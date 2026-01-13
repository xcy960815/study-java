// package com.example.config;
//
//
//
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.example.domain.vo.StudyJavaSysUserVo;
// import com.example.mapper.StudyJavaSysUserMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
//
//
//
// @SpringBootTest
// public class MyBatisPlusTest {
//
//        @Autowired
//        private StudyJavaSysUserMapper studyJavaUserMapper;
//
//    @Test
//    public void testGetUserList() {
//        Page<StudyJavaSysUserVo> page = new Page<>(1, 3);
//        StudyJavaSysUserVo studyJavaUser = new StudyJavaSysUserVo();
//        studyJavaUserMapper.getUserList(page, studyJavaUser);
//        System.out.println(page);
//        System.out.println(page.getRecords());//获取分页记录
//        System.out.println(page.getPages());//总页数
//        System.out.println(page.getTotal());//总记录数
//        System.out.println(page.hasNext());//是否有下一页
//        System.out.println(page.hasPrevious());//是否有上一页
//    }
// }
