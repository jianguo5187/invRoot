package com.ruoyi.project.system.service;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ProductTransaction;
import com.ruoyi.system.domain.SysActivationCode;
import com.ruoyi.system.domain.vo.CurrentInventoryRespVo;
import com.ruoyi.system.domain.vo.TodayProductTransactionRespVo;
import com.ruoyi.system.service.IProductService;
import com.ruoyi.system.service.ISysActivationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.activation.enabled:true}")
    private boolean activationEnabled;

    @Autowired
    private IProductService productService;

    @Autowired
    private ISysActivationCodeService activationCodeService;

    // 存储用户状态：key=chatId, value=状态
    private static final Map<String, String> userStates = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws TelegramApiException {
        setBotCommands();
    }

    private void setBotCommands() throws TelegramApiException {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("start", "开始使用"));
        if (activationEnabled) {
            commands.add(new BotCommand("activate", "激活账户"));
        }
        commands.add(new BotCommand("help", "帮助信息"));

        execute(SetMyCommands.builder()
                .scope(BotCommandScopeDefault.builder().build())
                .commands(commands)
                .build());
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                handleCallbackQuery(update.getCallbackQuery());
            } else if (update.hasMessage() && update.getMessage().hasText()) {
                handleTextMessage(update);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        boolean isGroup = isGroupMessage(callbackQuery.getMessage());
//        String userName = callbackQuery.getFrom().getUserName();
        String userName = "";
        if(isGroup){
            userName = callbackQuery.getMessage().getChat().getTitle();
        }else{
            userName = callbackQuery.getMessage().getFrom().getFirstName();
        }

        // 检查激活状态（群组除外）
        if (!isGroup && activationEnabled && !isUserActivated(chatId)) {
            sendResponse(chatId, "❌ 请先使用/activate命令激活您的账户");
            answerCallbackQuery(callbackQuery.getId());
            return;
        }

        if (callbackData.startsWith("menu_")) {
            switch (callbackData.substring(5)) {
//                case "set_price":
//                    userStates.put(getUserKey(chatId, isGroup), "AWAITING_PRODUCT_PRICE");
//                    sendResponse(chatId, "请输入商品名称和价格，用逗号分隔\n例如：苹果,5.5");
//                    break;
                case "set_price":
                    userStates.put(getUserKey(chatId, isGroup), "AWAITING_PRODUCT_PRICE");
                    sendResponse(chatId, "📝 请输入商品定价（可多行，每行一个商品）\n" +
                            "格式：商品名称,价格\n" +
                            "示例：\n" +
                            "苹果，5.5\n" +
                            "香蕉，3.2\n" +
                            "牛奶，12.8");
                    break;
                case "in_out":
                    userStates.put(getUserKey(chatId, isGroup), "AWAITING_PRODUCT_QUANTITY");
                    sendResponse(chatId, "📝 请输入商品入出库记录（可多行，每行一个商品）\n" +
                            "格式：商品名称，数量（正数入库，负数出库）\n" +
                            "示例：\n" +
                            "苹果，10\n" +
                            "香蕉，-5\n" +
                            "牛奶，20");
                    break;
//                case "in_out":
//                    userStates.put(getUserKey(chatId, isGroup), "AWAITING_PRODUCT_QUANTITY");
//                    sendResponse(chatId, "请输入商品名称和数量(正数入库，负数出库)\n例如：苹果，10 或 苹果，-5");
//                    break;
                case "today_list":
                    handleTodayList(chatId, isGroup);
                    break;
                case "history":
                    userStates.put(getUserKey(chatId, isGroup), "AWAITING_HISTORY_PRODUCT");
                    sendResponse(chatId, "请输入要查询的日期\n例如：2025-01-01");
                    break;
                case "current_stock":
                    handleCurrentStock(chatId, isGroup);
                    break;
                case "delete_data":
                    sendDeleteConfirmation(chatId);
                    break;
                case "help":
                    sendHelpMessage(chatId);
                    break;
            }
        }else if (callbackData.startsWith("delete_confirm_")) {
            String action = callbackData.substring(15); // 获取 confirm 或 cancel
            if ("confirm".equals(action)) {
                boolean success = productService.deleteAllData(botToken, chatId, isGroup);
                sendResponse(chatId, success ? "✅ 已删除所有商品数据" : "❌ 删除数据失败");
            } else {
                sendResponse(chatId, "❎ 已取消删除操作");
            }
            userStates.remove(getUserKey(chatId, isGroup));
            showMainMenu(chatId);
        }
        answerCallbackQuery(callbackQuery.getId());
    }

    private void handleTextMessage(Update update) throws TelegramApiException {
        String messageText = update.getMessage().getText().trim();
        long chatId = update.getMessage().getChatId();
        boolean isGroup = isGroupMessage(update.getMessage());
        String userName = "";
        if(isGroup){
            userName = update.getMessage().getChat().getTitle();
        }else{
            userName = update.getMessage().getFrom().getFirstName();
        }
        // 处理状态输入
        String userKey = getUserKey(chatId, isGroup);
        String userState = userStates.get(userKey);

        // 处理激活命令
        if (activationEnabled && messageText.startsWith("/activate")) {
            if(!isUserActivated(chatId)){
                handleActivation(chatId, userName, isGroup, messageText);
            }else{
                showMainMenu(chatId);
            }
            return;
        }

        // 检查激活状态（群组除外）
        if (!StringUtils.equals(userState,"AWAITING_ACTIVATION_CODE") && activationEnabled && !isUserActivated(chatId)) {
            sendResponse(chatId, "🔐 您需要先激活账户才能使用本机器人\n\n请发送 /activate 开始激活流程");
            return;
        }

        if (userState != null) {
            switch (userState) {
                case "AWAITING_ACTIVATION_CODE":
//                    if(!isGroup){
                        processActivationCode(chatId, userName, isGroup, messageText);
//                    }
                    return;
                case "AWAITING_PRODUCT_PRICE":
                    handleSetPriceInput(chatId, isGroup, messageText, userName);
                    return;
                case "AWAITING_PRODUCT_QUANTITY":
                    handleInventoryInput(chatId, isGroup, messageText, userName);
                    return;
                case "AWAITING_HISTORY_PRODUCT":
                    handleHistoryInput(chatId, isGroup, messageText);
                    return;
            }
        }

        // 处理常规命令
        if (messageText.startsWith("/start") || messageText.startsWith("/menu")) {
            showMainMenu(chatId);
        } else if (messageText.startsWith("/help")) {
            sendHelpMessage(chatId);
        }
    }

    // 生成用户/群组的唯一key
    private String getUserKey(long chatId, boolean isGroup) {
        return isGroup ? "group_" + chatId : "user_" + chatId;
    }

    private void handleActivation(long chatId, String chatName, boolean isGroup, String message) {
        String[] parts = message.split(" ");
        if (parts.length == 1) {
            userStates.put(getUserKey(chatId, isGroup), "AWAITING_ACTIVATION_CODE");
            sendResponse(chatId, "📝 请输入您的激活码：");
        } else {
            processActivationCode(chatId, chatName, isGroup, parts[1]);
        }
    }

    private void processActivationCode(long chatId, String chatName, boolean isGroup, String code) {
        try {
            SysActivationCode activationCode = activationCodeService.selectByCode(code);

            if (activationCode == null) {
                sendResponse(chatId, "❌ 激活码无效");
            } else if ("1".equals(activationCode.getStatus())) {
                sendResponse(chatId, "⚠️ 该激活码已被使用");
            } else if (activationCode.getExpireTime() != null &&
                    activationCode.getExpireTime().before(new Date())) {
                sendResponse(chatId, "⏳ 激活码已过期");
            } else {
                activationCode.setStatus("1");
                activationCode.setTelegramId(chatId);
                activationCode.setTelegramName(chatName);
                activationCode.setIsGroup(isGroup?"1":"0");
                activationCodeService.updateActivationCode(activationCode);
                userStates.remove(getUserKey(chatId, isGroup)); // 处理完成后移除状态
                sendResponse(chatId, "✅ 激活成功！您现在可以使用所有功能了");
                showMainMenu(chatId);
            }
        } catch (Exception e) {
            sendResponse(chatId, "⚠️ 处理激活码时出错，请重试");
        }
    }

    private void handleSetPriceInput(long chatId, boolean isGroup, String input, String operator) {
        String userKey = getUserKey(chatId, isGroup);
        String[] lines = input.split("\n"); // 按行分割输入

        // 验证至少有一行数据
        if (lines.length == 0) {
            sendResponse(chatId, "❌ 请输入至少一个商品定价\n格式：商品名称,价格\n例如：\n苹果，5.5\n西瓜，5.5");
            userStates.put(userKey, "AWAITING_PRODUCT_PRICE");
            return;
        }

        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int successCount = 0;
        int errorCount = 0;

        // 处理每一行
        for (String line : lines) {
            String[] parts = line.split("，");
            if (parts.length != 2) {
                errorMsg.append("❌ 格式错误：").append(line).append(" (需要：商品，价格)\n");
                errorCount++;
                continue;
            }

            try {
                String productName = parts[0].trim();
                double price = Double.parseDouble(parts[1].trim());
                boolean success = productService.setProductPrice(botToken, chatId, isGroup, productName, price);

                if (success) {
                    successMsg.append("✅ ").append(productName).append(": ").append(price).append("\n");
                    successCount++;
                } else {
                    errorMsg.append("❌ 保存失败：").append(line).append("\n");
                    errorCount++;
                }
            } catch (NumberFormatException e) {
                errorMsg.append("❌ 价格无效：").append(line).append(" (必须是数字)\n");
                errorCount++;
            }
        }

        // 构建结果消息
        StringBuilder response = new StringBuilder();
        if (successCount > 0) {
            response.append("成功设置 ").append(successCount).append(" 个商品价格：\n").append(successMsg);
            userStates.remove(userKey); // 处理完成后移除状态
        }
        if (errorCount > 0) {
            response.append("\n失败 ").append(errorCount).append(" 条：\n").append(errorMsg);
        }

        sendResponse(chatId, response.toString());
        showMainMenu(chatId);

//        String[] parts = input.split("，");
//        if (parts.length != 2) {
//            sendResponse(chatId, "格式错误，请输入：商品名称 价格\n例如：\n苹果，5.5\n西瓜，5.5");
//            // 保持状态不变，等待用户重新输入
//            userStates.put(userKey, "AWAITING_PRODUCT_PRICE");
//            return;
//        }
//
//        try {
//            String productName = parts[0];
//            double price = Double.parseDouble(parts[1]);
//            boolean success = productService.setProductPrice(botToken, chatId, isGroup, productName, price);
//            if (success) {
//                sendResponse(chatId, String.format("✅ 已设置商品【%s】价格为: %.2f", productName, price));
//                userStates.remove(userKey); // 只有成功时才移除状态
//            } else {
//                sendResponse(chatId, "❌ 设置商品价格失败");
//                // 保持状态不变，等待用户重新输入
//                userStates.put(userKey, "AWAITING_PRODUCT_PRICE");
//            }
//        } catch (NumberFormatException e) {
//            sendResponse(chatId, "价格必须是数字，例如: 5.5");
//            // 保持状态不变，等待用户重新输入
//            userStates.put(userKey, "AWAITING_PRODUCT_PRICE");
//        }
    }

    private void handleInventoryInput(long chatId, boolean isGroup, String input, String operator) {
        String userKey = getUserKey(chatId, isGroup);
        String[] lines = input.split("\n"); // 按行分割输入

        // 验证至少有一行数据
        if (lines.length == 0) {
            sendResponse(chatId, "❌ 请输入至少一条入出库记录\n格式：商品名称，数量\n示例：\n苹果，10\n香蕉，-5");
            userStates.put(userKey, "AWAITING_PRODUCT_QUANTITY");
            return;
        }

        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int successCount = 0;
        int errorCount = 0;

        // 处理每一行
        for (String line : lines) {
            String[] parts = line.split("，"); // 使用全角逗号分隔
            if (parts.length != 2) {
                errorMsg.append("❌ 格式错误：").append(line).append(" (需要：商品，数量)\n");
                errorCount++;
                continue;
            }

            try {
                String productName = parts[0].trim();
                double quantity = Double.parseDouble(parts[1].trim());

                // 预检查
                String preCheckResult = productService.inventoryPreCheck(botToken, chatId, isGroup, productName, quantity);
                if (StringUtils.isNotEmpty(preCheckResult)) {
                    errorMsg.append("❌ ").append(productName).append(": ").append(preCheckResult).append("\n");
                    errorCount++;
                    continue;
                }

                // 处理入出库
                boolean success = productService.processInventory(botToken, chatId, isGroup, productName, quantity, operator);
                if (success) {
                    successMsg.append("✅ ").append(productName)
                            .append(": ").append(quantity > 0 ? "入库" : "出库")
                            .append(Math.abs(quantity)).append("\n");
                    successCount++;
                } else {
                    errorMsg.append("❌ 操作失败：").append(line).append("\n");
                    errorCount++;
                }
            } catch (NumberFormatException e) {
                errorMsg.append("❌ 数量无效：").append(line).append(" (必须是数字)\n");
                errorCount++;
            }

        }

        // 构建结果消息
        StringBuilder response = new StringBuilder();
        if (successCount > 0) {
            response.append("成功处理 ").append(successCount).append(" 条记录：\n").append(successMsg);

            response.append("📋 今日入出库列表:\n");
            response.append("品类 | 数量 | 单价 | 总额\n");
            List<TodayProductTransactionRespVo> todayList = productService.getTodayTransactions(botToken, chatId, isGroup);
            Double todayQtySum = 0.00;
            Double todayAmountSum = 0.00;
            for(TodayProductTransactionRespVo t : todayList){

                response.append(String.format("%s | %.2f | %.2f | %.2f\n",
                        t.getProductName(),
                        t.getQuantity(),
                        t.getPrice(),
                        t.getTotalAmount()));
//                response.append(t.getProductName()
//                        + " | "+ t.getQuantity()
//                        + " | " + t.getPrice()
//                        + " | " + t.getTotalAmount()
//                        + "\n");

                todayQtySum += t.getQuantity();
                todayAmountSum += t.getTotalAmount();
            }
            response.append(String.format("共计 | %.2f |  | %.2f\n", todayQtySum, todayAmountSum));
//            response.append("共计 | " + todayQtySum + " |  | " + todayAmountSum +"\n");
//            sendResponse(chatId, response.toString());
            userStates.remove(userKey); // 只有成功时才移除状态
        }

        if (errorCount > 0) {
            response.append("\n失败 ").append(errorCount).append(" 条：\n").append(errorMsg);
        }

        sendResponse(chatId, response.toString());
        showMainMenu(chatId);
//        userStates.remove(userKey); // 处理完成后移除状态

//        String[] parts = input.split("，");
//        if (parts.length != 2) {
//            sendResponse(chatId, "格式错误，请输入：商品名称，数量\n示例：\n苹果，10\n香蕉，-5");
//            // 保持状态不变，等待用户重新输入
//            userStates.put(userKey, "AWAITING_PRODUCT_QUANTITY");
//            return;
//        }
//
//        try {
//            String productName = parts[0];
//            int quantity = Integer.parseInt(parts[1]);
//            String preCheckReuslt = productService.inventoryPreCheck(botToken, chatId,isGroup,productName,quantity);
//            if(StringUtils.isNotEmpty(preCheckReuslt)){
//                sendResponse(chatId, "❌ 商品入出库失败：" + preCheckReuslt);
//            }else{
//                boolean success = productService.processInventory(botToken, chatId, isGroup, productName, quantity, operator);
//                if (success) {
//                    StringBuilder response = new StringBuilder();
//                    response.append(String.format("✅ 已%s商品【%s】数量: %d\n\n",
//                            quantity > 0 ? "入库" : "出库", productName, Math.abs(quantity)));
//
//                    response.append("📋 今日入出库列表:\n");
//                    response.append("品类 | 数量 | 单价 | 总额\n");
//                    List<TodayProductTransactionRespVo> todayList = productService.getTodayTransactions(botToken, chatId, isGroup);
//                    Integer todayQtySum = 0;
//                    Double todayAmountSum = 0.00;
//                    for(TodayProductTransactionRespVo t : todayList){
//
//                        response.append(t.getProductName()
//                                + " | "+ t.getQuantity()
//                                + " | " + t.getPrice()
//                                + " | " + t.getTotalAmount()
//                                + "\n");
//
//                        todayQtySum += t.getQuantity();
//                        todayAmountSum += t.getTotalAmount();
//                    }
//                    response.append("共计 | " + todayQtySum + " |  | " + todayAmountSum +"\n");
//                    sendResponse(chatId, response.toString());
//                    userStates.remove(userKey); // 只有成功时才移除状态
//                } else {
//                    sendResponse(chatId, "❌ 商品入出库操作失败");
//                    // 保持状态不变，等待用户重新输入
//                    userStates.put(userKey, "AWAITING_PRODUCT_QUANTITY");
//                }
//            }
//        } catch (NumberFormatException e) {
//            sendResponse(chatId, "数量必须是整数，例如: 10 或 -5");
//            // 保持状态不变，等待用户重新输入
//            userStates.put(userKey, "AWAITING_PRODUCT_QUANTITY");
//        }
    }

    private void handleTodayList(long chatId, boolean isGroup) {
        List<TodayProductTransactionRespVo> todayList = productService.getTodayTransactions(botToken, chatId, isGroup);
        if (todayList.isEmpty()) {
            sendResponse(chatId, "今日尚无入出库记录");
            return;
        }

        StringBuilder response = new StringBuilder("📋 今日入出库列表:\n");
        response.append("品类 | 数量 | 单价 | 总额\n");
        Double todayQtySum = 0.00;
        Double todayAmountSum = 0.00;
        for(TodayProductTransactionRespVo t : todayList){

            response.append(String.format("%s | %.2f | %.2f | %.2f\n",
                    t.getProductName(),
                    t.getQuantity(),
                    t.getPrice(),
                    t.getTotalAmount()));
//            response.append(t.getProductName()
//                    + " | "+ t.getQuantity()
//                    + " | " + t.getPrice()
//                    + " | " + t.getTotalAmount()
//                    + "\n");

            todayQtySum += t.getQuantity();
            todayAmountSum += t.getTotalAmount();
        }
//        response.append("共计 | " + todayQtySum + " |  | " + todayAmountSum +"\n");
        response.append(String.format("共计 | %.2f |  | %.2f\n", todayQtySum, todayAmountSum));
        sendResponse(chatId, response.toString());
    }

    private void handleHistoryInput(long chatId, boolean isGroup, String historyDate) {
        String userKey = getUserKey(chatId, isGroup);

        // 日期格式验证 (yyyy-MM-dd)
        if (!historyDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            sendResponse(chatId, "❌ 日期格式不正确，请输入 yyyy-MM-dd 格式的日期\n例如：2025-01-01");
            userStates.put(userKey, "AWAITING_HISTORY_PRODUCT"); // 保持状态等待重新输入
            return;
        }
        try {
            // 进一步验证日期的有效性
            java.time.LocalDate.parse(historyDate);

            List<TodayProductTransactionRespVo> history = productService.getTransactionHistory(botToken, chatId, isGroup, historyDate);
            if (history.isEmpty()) {
                sendResponse(chatId, String.format("日期【%s】尚无入出库记录", historyDate));
                return;
            }

            StringBuilder response = new StringBuilder(String.format("📜 日期【%s】入出库历史:\n", historyDate));
            response.append("品类 | 数量 | 单价 | 总额\n");
            Double todayQtySum = 0.00;
            Double todayAmountSum = 0.00;
            for(TodayProductTransactionRespVo t : history){

                response.append(String.format("%s | %.2f | %.2f | %.2f\n",
                        t.getProductName(),
                        t.getQuantity(),
                        t.getPrice(),
                        t.getTotalAmount()));
//                response.append(t.getProductName()
//                        + " | "+ t.getQuantity()
//                        + " | " + t.getPrice()
//                        + " | " + t.getTotalAmount()
//                        + "\n");

                todayQtySum += t.getQuantity();
                todayAmountSum += t.getTotalAmount();
            }
//            response.append("共计 | " + todayQtySum + " |  | " + todayAmountSum +"\n");
            response.append(String.format("共计 | %.2f |  | %.2f\n", todayQtySum, todayAmountSum));
            sendResponse(chatId, response.toString());
            userStates.remove(userKey); // 查询完成后移除状态
        } catch (java.time.format.DateTimeParseException e) {
            sendResponse(chatId, "❌ 无效的日期，请输入有效的日期\n例如：2025-01-01");
            userStates.put(userKey, "AWAITING_HISTORY_PRODUCT"); // 保持状态等待重新输入
        }
    }

    private void handleCurrentStock(long chatId, boolean isGroup) {
        List<CurrentInventoryRespVo> stockList = productService.getCurrentInventory(botToken, chatId, isGroup);
        if (stockList.isEmpty()) {
            sendResponse(chatId, "当前没有库存记录");
            return;
        }

        StringBuilder response = new StringBuilder("📦 当前库存列表:\n");
        response.append("品类 | 数量 | 单价 | 总额\n");
        Double todayQtySum = 0.00;
        Double todayAmountSum = 0.00;
        for(CurrentInventoryRespVo t : stockList){

            response.append(String.format("%s | %.2f | %.2f | %.2f\n",
                    t.getProductName(),
                    t.getQuantity(),
                    t.getPrice(),
                    t.getTotalAmount()));
//            response.append(t.getProductName()
//                    + " | "+ t.getQuantity()
//                    + " | " + t.getPrice()
//                    + " | " + t.getTotalAmount()
//                    + "\n");

            todayQtySum += t.getQuantity();
            todayAmountSum += t.getTotalAmount();
        }
//        response.append("共计 | " + todayQtySum + " |  | " + todayAmountSum +"\n");/**/
        response.append(String.format("共计 | %.2f |  | %.2f\n", todayQtySum, todayAmountSum));

        sendResponse(chatId, response.toString());
    }

    private void sendDeleteConfirmation(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("⚠️ 确定要删除所有数据吗？此操作不可恢复！");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // 确认行按钮
        rows.add(Arrays.asList(
                createInlineButton("✅ 确定删除", "delete_confirm_confirm"),
                createInlineButton("❌ 取消", "delete_confirm_cancel")
        ));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteData(long chatId, boolean isGroup, String operator) {
        boolean success = productService.deleteAllData(botToken, chatId, isGroup);
        sendResponse(chatId, success ? "✅ 已删除所有商品数据" : "❌ 删除数据失败");
    }

    private void showMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("请选择操作：");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // 第一行按钮
        rows.add(Arrays.asList(
                createInlineButton("商品定价", "menu_set_price"),
                createInlineButton("商品入出库", "menu_in_out")
        ));

        // 第二行按钮
        rows.add(Arrays.asList(
                createInlineButton("当日列表", "menu_today_list"),
                createInlineButton("历史记录", "menu_history")
        ));

        // 第三行按钮
        rows.add(Arrays.asList(
                createInlineButton("当前库存", "menu_current_stock"),
                createInlineButton("删除数据", "menu_delete_data")
        ));

        // 第四行按钮
        rows.add(Arrays.asList(
                createInlineButton("帮助", "menu_help")
        ));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendHelpMessage(long chatId) {
        StringBuilder helpText = new StringBuilder("🆘 帮助菜单\n\n")
                .append("1. 点击【商品定价】设置商品价格\n")
                .append("2. 点击【商品入出库】管理库存\n")
                .append("3. 点击【当日列表】查看当天入出库记录\n")
                .append("4. 点击【历史记录】查询指定日期的入出库记录\n")
                .append("5. 点击【当前库存】查看当前库存\n")
                .append("6. 点击【删除数据】清除所有记录");

        if (activationEnabled) {
            helpText.append("\n\n使用/activate命令激活账户");
        }

        sendResponse(chatId, helpText.toString());
    }

    private InlineKeyboardButton createInlineButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }

    private void sendResponse(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void answerCallbackQuery(String callbackQueryId) throws TelegramApiException {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackQueryId);
        execute(answer);
    }

    private boolean isGroupMessage(org.telegram.telegrambots.meta.api.objects.Message message) {
        return message.isGroupMessage() || message.isSuperGroupMessage();
    }

    private boolean isUserActivated(long chatId) {
        return activationCodeService.selectByTelegramId(chatId) != null;
    }
}