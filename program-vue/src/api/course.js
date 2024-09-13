import request from '@/utils/request'

export default {
    
    getListInfo (data) {
        return request({
            url: '/teacher/coursebase/list',
            method: 'post',
            data: data
        })
    },   
    getCourseList (data) {
        return request({
            url: '/public/course/list',
            method: 'post',
            data: data
        })
    },
    getCourseInfo(id) {
        return request({
        url: `/public/course/getCourseInfo/${id}`,
        method: 'get'
        })
    },
    addCategory(data){
        return request({
            url: '/teacher/coursebase/addCategory',
            method: 'post',
            data: data
        })
    },
    // 查询审计
    queryAudit (id) {
        return request({
        url: `/teacher/coursebase/queryAudit/${id}`,
        method: 'get'
        })
    },
    // 查询需要编辑的课程信息
    getCategoryInfo(id){
        return request({
        url: `/teacher/coursebase/getCategoryInfo/${id}`,
        method: 'get'
        })
    },
    updateCourse(data){
        return request({
            url: '/teacher/coursebase/updateCourse',
            method: 'post',
            data: data
        })
    },
    deleteCourse(id){
        return request({
        url: `/teacher/coursebase/deleteCourse/${id}`,
        method: 'get'
        })
    },
    handle(id, params) {
        return request({
          url: `/teacher/coursebase/handle/${id}`,
          method: 'get',
          params: params
        })
    },
    announce(id){
    return request({
        url: `/teacher/coursebase/announce/${id}`,
        method: 'get'
        })
    },
}