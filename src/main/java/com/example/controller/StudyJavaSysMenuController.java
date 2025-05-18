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
import org.springframework.beans.BeanUtils;
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
     * @param menuDto 查询条件
     * @return 菜单列表
     */
    @GetMapping("/list")
    public ResponseResult<Map<String,Object>> list(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaSysMenuDto menuDto
    ) {
        // 将DTO转换为VO用于查询
        StudyJavaSysMenuVo menuVo = new StudyJavaSysMenuVo();
        BeanUtils.copyProperties(menuDto, menuVo);

        IPage<StudyJavaSysMenuDto> menuPage = studyJavaSysMenuService.list(startPage(pageNum, pageSize), menuVo);
        return ResponseGenerator.generateSuccessResult(getPageData(menuPage));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 菜单详情
     */
    @GetMapping("/getDetail/{id}")
    public ResponseResult<StudyJavaSysMenuVo> getDetail(@PathVariable Serializable id) {
        StudyJavaSysMenuDto menuDto = studyJavaSysMenuService.getDetail(id);
        if (menuDto == null) {
//            return ResponseGenerator.generateErrorResult("菜单不存在");
        }

        // 将DTO转换为VO
        StudyJavaSysMenuVo menuVo = new StudyJavaSysMenuVo();
        BeanUtils.copyProperties(menuDto, menuVo);

        return ResponseGenerator.generateSuccessResult(menuVo);
    }

    /**
     * 新增菜单
     *
     * @param menuDto 菜单信息
     * @return 新增结果
     */
    @PostMapping("/addMenu")
    public ResponseResult<StudyJavaSysMenuVo> addMenu(@Valid @RequestBody StudyJavaSysMenuDto menuDto) {
        // 将DTO转换为VO
        StudyJavaSysMenuVo menuVo = new StudyJavaSysMenuVo();
        BeanUtils.copyProperties(menuDto, menuVo);

        Boolean result = studyJavaSysMenuService.addMenu(menuVo);

        if (result) {
            // 获取新增的菜单信息
            StudyJavaSysMenuDto newMenuDto = studyJavaSysMenuService.getDetail(menuVo.getMenuId());
            StudyJavaSysMenuVo newMenuVo = new StudyJavaSysMenuVo();
            BeanUtils.copyProperties(newMenuDto, newMenuVo);
            return ResponseGenerator.generateSuccessResult(newMenuVo);
        } else {
//            return ResponseGenerator.<StudyJavaSysMenuVo>generateErrorResult("新增菜单失败");
            return null;
        }
    }

    /**
     * 更新菜单
     *
     * @param menuDto 菜单信息
     * @return 更新结果
     */
    @PutMapping("/updateMenu")
    public ResponseResult<StudyJavaSysMenuVo> updateMenu(@Valid @RequestBody StudyJavaSysMenuDto menuDto) {
        if (menuDto.getMenuId() == null) {
//            return ResponseGenerator.<StudyJavaSysMenuVo>generateErrorResult("菜单ID不能为空");
        }

        // 将DTO转换为VO
        StudyJavaSysMenuVo menuVo = new StudyJavaSysMenuVo();
        BeanUtils.copyProperties(menuDto, menuVo);

        Boolean result = studyJavaSysMenuService.updateMenu(menuVo);

        if (result) {
            // 获取更新后的菜单信息
            StudyJavaSysMenuDto updatedMenuDto = studyJavaSysMenuService.getDetail(menuDto.getMenuId());
            StudyJavaSysMenuVo updatedMenuVo = new StudyJavaSysMenuVo();
            BeanUtils.copyProperties(updatedMenuDto, updatedMenuVo);
            return ResponseGenerator.generateSuccessResult(updatedMenuVo);
        } else {
//            return ResponseGenerator.generateErrorResult("更新菜单失败");
            return null;
        }
    }

    /**
     * 删除菜单（软删除）
     *
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
        IPage<StudyJavaSysMenuDto> allMenus = studyJavaSysMenuService.list(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 1000), new StudyJavaSysMenuVo());
        List<StudyJavaSysMenuDto> menuList = allMenus.getRecords();

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
    private List<Map<String, Object>> buildMenuTree(List<StudyJavaSysMenuDto> menuList, Long parentId) {
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

