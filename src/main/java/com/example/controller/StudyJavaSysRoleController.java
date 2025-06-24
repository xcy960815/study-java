package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.StudyJavaSysRoleDto;
import com.example.domain.vo.StudyJavaSysRoleVo;
import com.example.service.StudyJavaSysRoleService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseListResult;
import com.example.utils.ResponseResult;
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
    @GetMapping("/getAllRoleList")
    public ResponseListResult<StudyJavaSysRoleVo> getAllRoleList() {
        List<StudyJavaSysRoleVo> list = studyJavaSysRoleService.getAllRoleList();
        return ResponseGenerator.generateListResult(list, list.size());
    }

    /**
     * 获取角色详细信息
     */
    @GetMapping("/getRoleInfo")
    public ResponseResult<StudyJavaSysRoleVo> getRoleInfo(@RequestParam Long id) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysRoleService.getRoleInfo(id));
    }

    /**
     * 新增角色
     */
    @PostMapping("/insertRole")
    public ResponseResult<Boolean> insertRole(@Validated @RequestBody StudyJavaSysRoleDto roleDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysRoleService.insertRole(roleDto));
    }

    /**
     * 修改角色
     */
    @PutMapping("/updateRole")
    public ResponseResult<Boolean> updateRole(@Validated @RequestBody StudyJavaSysRoleDto roleDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysRoleService.updateRole(roleDto));
    }

    /**
     * 停用角色
     */
    @DeleteMapping("/deleteRole")
    public ResponseResult<Boolean> deleteRole(@RequestBody StudyJavaSysRoleDto roleDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysRoleService.deleteRole(roleDto));
    }

    /**
     * 启用角色
     */
    @PostMapping("/enableRole")
    public ResponseResult<Boolean> enableRole(@RequestBody StudyJavaSysRoleDto roleDto) {
        roleDto.setStatus(1);
        return ResponseGenerator.generateSuccessResult(studyJavaSysRoleService.updateRoleStatus(roleDto));
    }

    /**
     * 禁用角色
     */
    @PostMapping("/disableRole")
    public ResponseResult<Boolean> disableRole(@RequestBody StudyJavaSysRoleDto roleDto) {
        roleDto.setStatus(0);
        return ResponseGenerator.generateSuccessResult(studyJavaSysRoleService.updateRoleStatus(roleDto));
    }

}
