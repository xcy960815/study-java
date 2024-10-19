package com.example.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import com.example.service.StudyJavaUserService;
import java.util.List;

@Controller
@RequestMapping("/user") // 前缀
public class StudyJavaUserController {
    @Resource
    private StudyJavaUserService studyJavaUserService;
    // RequestParam 通常用于获取单个参数
    // ModelAttribute 通常用于获取多个参数
    @GetMapping("/getUserList")
    @ResponseBody
    public ResponseResult getUserList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("userQueryData") StudyJavaUser  userQueryData) {

        // 分页对象，传入当前页码和每页条数
        Page<StudyJavaUser> page = new Page<>(pageNum, pageSize);
        IPage<StudyJavaUser> userPage = studyJavaUserService.getUserList(page, userQueryData);
//        临时性写法
        class UserPageResult {
            private int total;
            private List<StudyJavaUser> data;
            public int getTotal() {
               return total;
           }
           public void setTotal(int total) {
                this.total = total;
           }
           public List<StudyJavaUser> getData() {
                return data;
           }
            public void setData(List<StudyJavaUser> data) {
                this.data = data;
            }
        }
        UserPageResult userPageResult = new UserPageResult();
        userPageResult.setData(userPage.getRecords());
        userPageResult.setTotal((int)userPage.getTotal());
        // 返回分页数据和总条数
        return ResponseGenerator.generatSuccessResult(userPageResult);
    }
}
