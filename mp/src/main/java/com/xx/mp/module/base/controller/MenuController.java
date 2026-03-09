package com.xx.mp.module.base.controller;

import com.xx.mp.module.base.service.MenuService;
import com.xx.mp.module.base.vo.ShowMenuVO;
import io.github.lujiafa.houtu.web.model.ResponseData;
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
