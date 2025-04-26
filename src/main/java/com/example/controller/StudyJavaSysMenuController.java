package com.example.controller;


import com.example.service.StudyJavaSysMenuService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 * 系统菜单表(StudyJavaSysMenuDao)表控制层
 *
 * @author makejava
 * @since 2025-04-25 18:27:53
 */
@RestController
@RequestMapping("studyJavaSysMenu")
public class StudyJavaSysMenuController {
    /**
     * 服务对象
     */
    @Resource
    private StudyJavaSysMenuService studyJavaSysMenuService;


}

