package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import com.example.service.StudyJavaSysMenuService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统菜单表(StudyJavaSysMenuDao)表控制层
 *
 * @author makejava
 * @since 2025-05-07 17:42:44
 */
@Slf4j
@RestController
@RequestMapping("/studyJavaSysMenu")
public class StudyJavaSysMenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private StudyJavaSysMenuService studyJavaSysMenuService;

    /**
     * 获取菜单列表
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @param studyJavaSysMenuDto 查询条件
     * @return 菜单列表
     */
    @GetMapping("/getMenuList")
    public ResponseResult<Map<String,Object>> getMenuList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaSysMenuDto studyJavaSysMenuDto
    ) {
        IPage<StudyJavaSysMenuVo> menuPage = studyJavaSysMenuService.getMenuList(startPage(pageNum, pageSize), studyJavaSysMenuDto);
        return ResponseGenerator.generateSuccessResult(getPageData(menuPage));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 菜单详情
     */
    @GetMapping("/getMenuDetail/{id}")
    public ResponseResult<StudyJavaSysMenuVo> getMenuDetail(@PathVariable Serializable id) {
        StudyJavaSysMenuVo studyJavaSysMenuVo = studyJavaSysMenuService.getMenuDetail(id);
        if (studyJavaSysMenuVo == null) {
//            return ResponseGenerator.generateErrorResult("菜单不存在");
        }
        return ResponseGenerator.generateSuccessResult(studyJavaSysMenuVo);
    }

    /**
     * 新增菜单
     *
     * @param studyJavaSysMenuDto 菜单信息
     * @return 新增结果
     */
    @PostMapping("/addMenu")
    public ResponseResult<StudyJavaSysMenuVo> addMenu(@Valid @RequestBody StudyJavaSysMenuDto studyJavaSysMenuDto) {
        Boolean result = studyJavaSysMenuService.addMenu(studyJavaSysMenuDto);
        if (result) {
            // 获取新增的菜单信息
            StudyJavaSysMenuVo studyJavaSysMenuVo = studyJavaSysMenuService.getMenuDetail(studyJavaSysMenuDto.getMenuId());
            return ResponseGenerator.generateSuccessResult(studyJavaSysMenuVo);
        } else {
//            return ResponseGenerator.<StudyJavaSysMenuVo>generateErrorResult("新增菜单失败");
            return null;
        }
    }

    /**
     * 更新菜单
     *
     * @param studyJavaSysMenuDto 菜单信息
     * @return 更新结果
     */
    @PutMapping("/updateMenu")
    public ResponseResult<StudyJavaSysMenuVo> updateMenu(@Valid @RequestBody StudyJavaSysMenuDto studyJavaSysMenuDto) {
        if (studyJavaSysMenuDto.getMenuId() == null) {
//            return ResponseGenerator.<StudyJavaSysMenuVo>generateErrorResult("菜单ID不能为空");
        }
        Boolean result = studyJavaSysMenuService.updateMenu(studyJavaSysMenuDto);
        if (result) {
            // 获取更新后的菜单信息
            StudyJavaSysMenuVo studyJavaSysMenuVo = studyJavaSysMenuService.getMenuDetail(studyJavaSysMenuDto.getMenuId());
            return ResponseGenerator.generateSuccessResult(studyJavaSysMenuVo);
        } else {
//            return ResponseGenerator.generateErrorResult("更新菜单失败");
            return null;
        }
    }

    /**
     * 删除菜单（软删除）
     * @param id 菜单ID
     * @return 删除结果
     */
    @DeleteMapping("/deleteMenu/{id}")
    public ResponseResult<Boolean> deleteMenu(@PathVariable Serializable id) {
        Boolean result = studyJavaSysMenuService.deleteMenu(id);
        if (result) {
            return ResponseGenerator.generateSuccessResult(true);
        } else {
//            return ResponseGenerator.<Boolean>generateErrorResult("删除菜单失败");
            return null;
        }
    }

    /**
     * 获取菜单树形结构
     * @return 菜单树
     */
    @GetMapping("/tree")
    public ResponseResult<List<Map<String, Object>>> getMenuTree() {
        // 获取所有菜单
        IPage<StudyJavaSysMenuVo> allMenus = studyJavaSysMenuService.getMenuList(startPage(1, 9999), new StudyJavaSysMenuDto());
        List<StudyJavaSysMenuVo> menuList = allMenus.getRecords();

        // 构建树形结构
        List<Map<String, Object>> menuTree = buildMenuTree(menuList, 0L);
        return ResponseGenerator.generateSuccessResult(menuTree);
    }

    /**
     * 递归构建菜单树
     * @param menuList 菜单列表
     * @param parentId 父菜单ID
     * @return 菜单树
     */
    private List<Map<String, Object>> buildMenuTree(List<StudyJavaSysMenuVo> menuList, Long parentId) {
        return menuList.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .map(menu -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", menu.getMenuId());
                    node.put("name", menu.getMenuName());
                    node.put("path", menu.getPath());
                    node.put("component", menu.getComponent());
                    node.put("icon", menu.getIcon());
                    node.put("type", menu.getMenuType());
                    node.put("perms", menu.getPerms());
                    node.put("orderNum", menu.getOrderNum());

                    List<Map<String, Object>> children = buildMenuTree(menuList, menu.getMenuId());
                    if (!children.isEmpty()) {
                        node.put("children", children);
                    }

                    return node;
                })
                .collect(Collectors.toList());
    }
}

