package com.example.controller;

import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.service.StudyJavaSysMenuService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单控制器
 */
@RestController
@RequestMapping("/sysMenu")
public class StudyJavaSysMenuController {

    @Resource
    private StudyJavaSysMenuService studyJavaSysMenuService;

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<StudyJavaSysMenuDto>> getMenuList(
            @RequestParam(required = false) String menuName,
            @RequestParam(required = false) Integer menuType,
            @RequestParam(required = false) Long parentId) {
        return ResponseEntity.ok(studyJavaSysMenuService.getMenuList(menuName, menuType, parentId));
    }

    /**
     * 根据ID获取菜单
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<StudyJavaSysMenuDto> getMenuById(@PathVariable Long menuId) {
        return ResponseEntity.ok(studyJavaSysMenuService.getMenuById(menuId));
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public ResponseEntity<Void> addMenu(@RequestBody StudyJavaSysMenuDto menuDto) {
        studyJavaSysMenuService.addMenu(menuDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新菜单
     */
    @PutMapping
    public ResponseEntity<Void> updateMenu(@RequestBody StudyJavaSysMenuDto menuDto) {
        studyJavaSysMenuService.updateMenu(menuDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        studyJavaSysMenuService.deleteMenu(menuId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取用户菜单列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StudyJavaSysMenuDto>> getUserMenuList(@PathVariable Long userId) {
        return ResponseEntity.ok(studyJavaSysMenuService.getUserMenuList(userId));
    }
}

