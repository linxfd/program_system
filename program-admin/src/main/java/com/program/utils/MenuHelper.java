package com.program.utils;

import com.program.model.entity.CourseCategory;
import com.program.model.entity.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 */
public class MenuHelper {

    /**
     * 使用递归方法建菜单
     * @param sysMenuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().longValue() == 0) {
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu it : treeNodes) {
            if(sysMenu.getId().longValue() == it.getParentId().longValue()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return sysMenu;
    }

    /**
     * 使用递归方法建课程分类
     * @param courseCategoryList
     * @return
     */
    public static List<CourseCategory> buildCategoryTree(List<CourseCategory> courseCategoryList) {
        List<CourseCategory> trees = new ArrayList<>();
        for (CourseCategory courseCategory : courseCategoryList) {
            if (courseCategory.getParentId().longValue() == 0) {
                trees.add(findCategoryChildren(courseCategory,courseCategoryList));
            }
        }
        return trees;
    }

    /**
     * 递归查找课程分类子节点
     * @param treeNodes
     * @return
     */
    public static CourseCategory findCategoryChildren(CourseCategory courseCategory, List<CourseCategory> treeNodes) {
        courseCategory.setChildren(new ArrayList<CourseCategory>());
        for (CourseCategory it : treeNodes) {
            if(courseCategory.getId().longValue() == it.getParentId().longValue()) {
                if (courseCategory.getChildren() == null) {
                    courseCategory.setChildren(new ArrayList<>());
                }
                courseCategory.getChildren().add(findCategoryChildren(it,treeNodes));
            }
        }
        if(EmptyUtil.isEmpty(courseCategory.getChildren())){
            courseCategory.setChildren(null);
        }
        return courseCategory;
    }
}
