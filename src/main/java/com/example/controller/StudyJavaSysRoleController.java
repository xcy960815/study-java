package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysRoleDao;
import com.example.domain.dto.StudyJavaSysRoleDto;
import com.example.domain.vo.StudyJavaSysRoleVo;
import com.example.service.StudyJavaSysRoleService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseListResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class StudyJavaSysRoleController {

    @Autowired
    private StudyJavaSysRoleService studyJavaSysRoleService;

    /**
     * 获取角色列表（分页）
     */
    @GetMapping("/getRoleList")
    public ResponseListResult<StudyJavaSysRoleVo> getRoleList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            StudyJavaSysRoleDto queryDto) {
        Page<StudyJavaSysRoleVo> page = studyJavaSysRoleService.getRoleList(pageNum, pageSize, queryDto);
        return ResponseGenerator.generateListResult(page.getRecords(), page.getTotal());
    }

    /**
     * 获取所有角色列表（不分页）
     */
    @GetMapping("/listAll")
    public ResponseListResult<StudyJavaSysRoleVo> listAll() {
        List<StudyJavaSysRoleVo> list = studyJavaSysRoleService.getAllRoleList();
        return ResponseGenerator.generateListResult(list, (long) list.size());
    }

    /**
     * 获取角色详细信息
     */
    @GetMapping("/{id}")
    public StudyJavaSysRoleVo getInfo(@PathVariable Long id) {
        StudyJavaSysRoleDao role = studyJavaSysRoleService.getById(id);
        if (role == null) {
            return null;
        }
        StudyJavaSysRoleVo vo = new StudyJavaSysRoleVo();
        BeanUtils.copyProperties(role, vo);
        return vo;
    }

    /**
     * 新增角色
     */
    @PostMapping
    public boolean add(@Validated @RequestBody StudyJavaSysRoleDto roleDto) {
        return studyJavaSysRoleService.insertRole(roleDto);
    }

    /**
     * 修改角色
     */
    @PutMapping
    public boolean edit(@Validated @RequestBody StudyJavaSysRoleDto roleDto) {
        return studyJavaSysRoleService.updateRole(roleDto);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public boolean remove(@PathVariable Long id) {
        return studyJavaSysRoleService.deleteRole(id);
    }

    /**
     * 修改角色状态
     */
    @PutMapping("/changeStatus")
    public boolean changeStatus(@RequestBody StudyJavaSysRoleDto roleDto) {
        return studyJavaSysRoleService.updateRoleStatus(roleDto);
    }
}
