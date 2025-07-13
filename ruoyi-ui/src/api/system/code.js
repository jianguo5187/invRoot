import request from '@/utils/request'

// 查询系统激活码列表
export function listCode(query) {
  return request({
    url: '/system/code/list',
    method: 'get',
    params: query
  })
}

// 查询系统激活码详细
export function getCode(id) {
  return request({
    url: '/system/code/' + id,
    method: 'get'
  })
}

// 新增系统激活码
export function addCode(data) {
  return request({
    url: '/system/code',
    method: 'post',
    data: data
  })
}

// 修改系统激活码
export function updateCode(data) {
  return request({
    url: '/system/code',
    method: 'put',
    data: data
  })
}

// 删除系统激活码
export function delCode(id) {
  return request({
    url: '/system/code/' + id,
    method: 'delete'
  })
}


// 新增激活码
export function batchAddActivationCode(data) {
  return request({
    url: '/system/code/batchGenerateActivationCode',
    method: 'post',
    data: data
  })
}
// 新增激活码
export function addActivationCode(data) {
  return request({
    url: '/system/code/generateActivationCode',
    method: 'post',
    data: data
  })
}
