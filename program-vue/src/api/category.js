
import request from '@/utils/request'

export default {
    getListInfo (data) {
        return request({
            url: '/teacher/coursebase/list',
            method: 'post',
            data: data
        })
    },
}