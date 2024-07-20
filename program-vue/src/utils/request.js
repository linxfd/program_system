import axios from 'axios'
import { Message } from 'element-ui'
import utils from '@/utils/utils'
import _ from 'lodash'
import { generateSign } from '@/utils/sign'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL, // api 的 base_url
  timeout: process.env.VUE_APP_REQUEST_TIME_OUT // 请求超时时间
})

service.interceptors.request.use(config => {
  const paramsObj = _.get(config, 'params', {})
  const paramsArr = []
  Object.keys(paramsObj)
    .filter(key => paramsObj[key] !== null)
    .map(key => {
      paramsArr.push(key + '=' + paramsObj[key])
    })
  const token = window.localStorage.getItem('authorization')
  const signHeaders = {
    'body-string': '',
    'query-string': encodeURI(_.join(paramsArr, '&')),
    'x-nonce': `${utils.getRandomId()}`,
    'x-timestamp': `${new Date().getTime()}`
  }
  if (config.data) {
    signHeaders['body-string'] = encodeURIComponent(JSON.stringify(config.data))
  }
  // add api sign
  config.headers.sign = generateSign(JSON.stringify(signHeaders))
  if (token) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
    config.headers.authorization = token
  }
  Object.assign(config.headers, signHeaders)
  return config
}, error => {
  return Promise.reject(error)
})

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非200是抛错 可结合自己业务进行修改
     */
    const responseData = response.data
    if (response.headers['content-type'] !== 'application/json') {
      return responseData
    }
    if (responseData.code !== 200) {
      Message({
        message: responseData.message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject('error')
    } else {
      return responseData
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.response.data.errMsg ?? '服务器错误',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
