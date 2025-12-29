package com.studyjava.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.annotation.Log;
import com.studyjava.domain.enums.BusinessType;
import com.studyjava.domain.dto.StudyJavaSysRoleDto;
import com.studyjava.domain.vo.StudyJavaSysRoleVo;
import com.studyjava.service.StudyJavaSysRoleService;
import com.studyjava.utils.PageResult;
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
    @Log(title = "角色管理", businessType = BusinessType.QUERY)
    @GetMapping("/getRoleList")
    public PageResult<StudyJavaSysRoleVo> getRoleList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            StudyJavaSysRoleDto queryDto) {
        Page<StudyJavaSysRoleVo> page = studyJavaSysRoleService.getRoleList(pageNum, pageSize, queryDto);
        return PageResult.of(page.getRecords(), page.getTotal());
    }

    /**
     * 获取所有角色列表（不分页）
     */
    @Log(title = "角色管理", businessType = BusinessType.QUERY)
    @GetMapping("/getAllRoleList")
    public List<StudyJavaSysRoleVo> getAllRoleList() {
        List<StudyJavaSysRoleVo> studyJavaSysRoleList = studyJavaSysRoleService.getAllRoleList();
        return studyJavaSysRoleList;
    }

    /**
     * 获取角色详细信息
     */
    @Log(title = "角色管理", businessType = BusinessType.QUERY)
    @GetMapping("/getRoleInfo")
    public StudyJavaSysRoleVo getRoleInfo(@RequestParam Long id) {
        StudyJavaSysRoleDto studyJavaSysRoleDto = new StudyJavaSysRoleDto();
        studyJavaSysRoleDto.setId(id);
        return studyJavaSysRoleService.getRoleInfo(studyJavaSysRoleDto);
    }

    /**
     * 新增角色
     */
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/insertRole")
    public Boolean insertRole(@Validated @RequestBody StudyJavaSysRoleDto roleDto) {
        return studyJavaSysRoleService.insertRole(roleDto);
    }

    /**
     * 修改角色
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateRole")
    public Boolean updateRole(@Validated @RequestBody StudyJavaSysRoleDto roleDto) {
        return studyJavaSysRoleService.updateRole(roleDto);
    }

    /**
     * 停用角色
     */
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteRole")
    public Boolean deleteRole(@RequestBody StudyJavaSysRoleDto roleDto) {
        return studyJavaSysRoleService.deleteRole(roleDto);
    }

    /**
     * 启用角色
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/enableRole")
    public Boolean enableRole(@RequestBody StudyJavaSysRoleDto roleDto) {
        roleDto.setStatus(1);
        return studyJavaSysRoleService.updateRoleStatus(roleDto);
    }

    /**
     * 禁用角色
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/disableRole")
    public Boolean disableRole(@RequestBody StudyJavaSysRoleDto roleDto) {
        roleDto.setStatus(0);
        return studyJavaSysRoleService.updateRoleStatus(roleDto);
    }

}
