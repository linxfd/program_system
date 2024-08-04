import request from '@/utils/request'

export default {
    getList(queryParams){
        return request({
            url: '/admin/website/list',
            method: 'post',
            data: queryParams
        })
    },

    getListByclassificationId(classificationId){
        return request({
            url: `/public/website/list/${classificationId}`,
            method: 'post',
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

    // 分类管理
    getClassificationList(queryParams){
        return request({
            url: '/public/website/ClassificationList',
            method: 'post',
            data: queryParams
        })
    },
    editClassification(queryParams){
        return request({
            url: '/admin/website/editClassification',
            method: 'post',
            data: queryParams
        })
    },
    removeClassification(ids){
        return request({
            url: `/admin/website/removeClassification/${ids}`,
            method: 'get',
        })
    },
    addClassification(queryParams){
        return request({
            url: '/admin/website/addClassification',
            method: 'post',
            data: queryParams
        })
    },
}