package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import com.example.service.StudyJavaSysMenuService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

/**
 * 系统菜单表(StudyJavaSysMenuDao)表控制层
 *
 * @author makejava
 * @since 2025-05-07 17:42:44
 */
@RestController
@RequestMapping("/studyJavaSysMenu")
public class StudyJavaSysMenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private StudyJavaSysMenuService studyJavaSysMenuService;

    @GetMapping("/list")
    public ResponseResult<Map<String,Object>> list(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaUser") StudyJavaSysMenuVo studyJavaSysMenuVo
    ) {
        IPage<StudyJavaSysMenuDto> menuPage = studyJavaSysMenuService.list(startPage(pageNum, pageSize), studyJavaSysMenuVo);
        return ResponseGenerator.generateSuccessResult(getPageData(menuPage));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/getDetail/{id}")
    public ResponseResult<StudyJavaSysMenuDto> getDetail(@PathVariable Serializable id) {
        StudyJavaSysMenuDto menu = studyJavaSysMenuService.getDetail(id);
        return ResponseGenerator.generateSuccessResult(menu);
    }

    /**
     * 新增菜单
     *
     * @param studyJavaSysMenuVo 菜单信息
     * @return 新增结果
     */
    @PostMapping("/addMenu")
    public ResponseResult<Boolean> addMenu(@RequestBody StudyJavaSysMenuVo studyJavaSysMenuVo) {
        Boolean result = studyJavaSysMenuService.addMenu(studyJavaSysMenuVo);
        return ResponseGenerator.generateSuccessResult(result);
    }

    /**
     * 更新菜单
     *
     * @param studyJavaSysMenuVo 菜单信息
     * @return 更新结果
     */
    @PutMapping("/updateMenu")
    public ResponseResult<Boolean> updateMenu(@RequestBody StudyJavaSysMenuVo studyJavaSysMenuVo) {
        Boolean result = studyJavaSysMenuService.updateMenu(studyJavaSysMenuVo);
        return ResponseGenerator.generateSuccessResult(result);
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
        return ResponseGenerator.generateSuccessResult(result);
    }

}

