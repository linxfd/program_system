import request from '@/utils/request'

export default {
    getList(queryParams){
        return request({
            url: '/public/website/list',
            method: 'post',
            data: queryParams
        })
    },
    add(queryParams){
        return request({
            url: '/admin/website/add',
            method: 'post',
            data: queryParams
        })
    },
    edit(queryParams){
        return request({
            url: '/admin/website/edit',
            method: 'post',
            data: queryParams
        })
    },

    remove(ids){
        return request({
            url: `/admin/website/remove/${ids}`,
            method: 'get',
        })
    },
    getClassidied(){
        return request({
            url: '/public/website/getClassidied',
            method: 'get',
        })
    },
}