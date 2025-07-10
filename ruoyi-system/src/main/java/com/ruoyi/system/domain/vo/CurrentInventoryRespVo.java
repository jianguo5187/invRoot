package com.ruoyi.system.domain.vo;

public class CurrentInventoryRespVo {

    /** 商品名称 */
    private String productName;

    /** 数量(正数入库，负数出库) */
    private Double quantity;

    /** 单价 */
    private Double price;

    /** 总额 */
    private Double totalAmount;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
