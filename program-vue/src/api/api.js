import request from '@/utils/request'

export default {
    postHttps (websiteUrl) {
        return request({
            url: `/util/fetchWebsite`,
            method: 'post',
            data: websiteUrl
        })
    },
}