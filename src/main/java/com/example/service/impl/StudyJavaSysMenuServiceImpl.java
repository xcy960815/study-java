package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.StudyJavaSysMenuMapper;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.example.service.StudyJavaSysMenuService;
import org.springframework.stereotype.Service;

/**
 * 系统菜单表(StudyJavaSysMenuDao)表服务实现类
 *
 * @author makejava
 * @since 2025-04-25 18:27:54
 */
@Service("studyJavaSysMenuService")
public class StudyJavaSysMenuServiceImpl extends ServiceImpl<StudyJavaSysMenuMapper, StudyJavaSysMenuDao> implements StudyJavaSysMenuService {

}

