import request from '@/utils/request'

export default {
    postHttps (websiteUrl) {
        return request({
            url: `/util/documentInfo`,
            method: 'post',
            data: websiteUrl
        })
    },
}