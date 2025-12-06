package com.studyjava.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dto.StudyJavaSysMenuDto;
import com.studyjava.domain.vo.StudyJavaSysMenuVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.service.StudyJavaSysMenuService;
import com.studyjava.utils.PageResult;
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
     * @param studyJavaSysMenuDto 查询条件
     * @return PageResult<StudyJavaSysMenuVo>
     */
    @GetMapping("/getMenuList")
    public PageResult<StudyJavaSysMenuVo> getMenuList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaSysMenuDto studyJavaSysMenuDto
    ) {
        IPage<StudyJavaSysMenuVo> menuPage = studyJavaSysMenuService.getMenuList(startPage(pageNum, pageSize), studyJavaSysMenuDto);
        return PageResult.of(menuPage.getRecords(), menuPage.getTotal());
    }

    /**
     * 获取当前用户所拥有的权限
     * @return List<StudyJavaSysMenuVo>
     */
    @GetMapping("/getRoutes")
    public List<StudyJavaSysMenuVo> getRoutes(){
        List<StudyJavaSysMenuVo> userMenuList = studyJavaSysMenuService.getRoutes();
        return buildMenuTree(userMenuList,null);
    }

    /**
     * 通过主键查询单条数据
     * @param id 主键
     * @return 菜单详情
     */
    @GetMapping("/getMenuInfo")
    public StudyJavaSysMenuVo getMenuInfo(@RequestParam Serializable id) {
        StudyJavaSysMenuVo studyJavaSysMenuVo = studyJavaSysMenuService.getMenuInfo(id);
        if (studyJavaSysMenuVo == null) {
            throw new StudyJavaException("菜单不存在");
        }
        return studyJavaSysMenuVo;
    }

    /**
     * 新增菜单
     * @param studyJavaSysMenuDto 菜单信息
     * @return 新增结果
     */
    @PostMapping("/insertMenu")
    public Boolean insertMenu(@Valid @RequestBody StudyJavaSysMenuDto studyJavaSysMenuDto) {
        return studyJavaSysMenuService.insertMenu(studyJavaSysMenuDto);
    }
    /**
     * 更新菜单
     *
     * @param studyJavaSysMenuDto 菜单信息
     * @return 更新结果
     */
    @PutMapping("/updateMenu")
    public Boolean updateMenu(@Valid @RequestBody StudyJavaSysMenuDto studyJavaSysMenuDto) {
        if (studyJavaSysMenuDto.getId() == null) {
            throw new StudyJavaException("菜单ID不能为空");
        }
        return studyJavaSysMenuService.updateMenu(studyJavaSysMenuDto);
    }

    /**
     * 删除菜单（软删除）
     * @param studyJavaSysMenuDto StudyJavaSysMenuDto
     * @return 删除结果
     */
    @DeleteMapping("/deleteMenu")
    public Boolean deleteMenu(@RequestBody StudyJavaSysMenuDto studyJavaSysMenuDto) {
        return studyJavaSysMenuService.deleteMenu(studyJavaSysMenuDto);
    }

    /**
     * 获取菜单树形结构
     * @return PageResult<StudyJavaSysMenuVo>
     */
    @GetMapping("/getMenuTree")
    public PageResult<StudyJavaSysMenuVo> getMenuTree(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaSysMenuDto studyJavaSysMenuDto
    ) {
        // 获取所有菜单
        IPage<StudyJavaSysMenuVo> allMenus = studyJavaSysMenuService.getMenuList(startPage(pageNum, pageSize), studyJavaSysMenuDto);
        List<StudyJavaSysMenuVo> menuList = allMenus.getRecords();
        // 构建树形结构
        List<StudyJavaSysMenuVo> menuTree = buildMenuTree(menuList, null);
        return PageResult.of(menuTree, menuTree.size());
    }

    /**
     * 获取所有菜单树
     */
    @GetMapping("/getAllMenuTree")
    public List<StudyJavaSysMenuVo> getAllMenuTree(){
        List<StudyJavaSysMenuVo> allMenuList  = studyJavaSysMenuService.getAllMenuList();
        return buildMenuTree(allMenuList, null);
    }

    /**
     * 递归构建菜单树
     * @param menuList 菜单列表
     * @param parentId 父菜单ID
     * @return 菜单树
     */
    private List<StudyJavaSysMenuVo> buildMenuTree(List<StudyJavaSysMenuVo> menuList, Long parentId) {
        return menuList.stream()
                .filter(menuOption -> Objects.equals(menuOption.getParentId(), parentId))
                .map(menuOption -> {
                    StudyJavaSysMenuVo studyJavaSysMenuVo = new StudyJavaSysMenuVo();
                    BeanUtils.copyProperties(menuOption, studyJavaSysMenuVo);
                    // 构建子菜单
                    List<StudyJavaSysMenuVo> children = buildMenuTree(menuList, menuOption.getId());
                    if (!children.isEmpty()) {
                        studyJavaSysMenuVo.setChildren(children);
                    }
                    return studyJavaSysMenuVo;
                })
                .collect(Collectors.toList());
    }
}

