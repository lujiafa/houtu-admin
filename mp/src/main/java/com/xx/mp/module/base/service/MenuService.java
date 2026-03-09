package com.xx.mp.module.base.service;

import com.xx.mp.module.base.vo.ShowMenuVO;

import java.util.List;

public interface MenuService {

    /**
     * 获取登录用户菜单列表
     * @return 菜单列表
     */
    List<ShowMenuVO> selectLoginUserMenuList();
}
