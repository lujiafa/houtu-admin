package com.houtu.mp.module.base.controller;

import com.houtu.mp.module.base.service.MenuService;
import com.houtu.mp.module.base.vo.ShowMenuVO;
import com.houtu.web.model.response.ResponseData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/mylist")
    public ResponseData<List<ShowMenuVO>> mylist() {
        return ResponseData.success(menuService.selectLoginUserMenuList());
    }
}
