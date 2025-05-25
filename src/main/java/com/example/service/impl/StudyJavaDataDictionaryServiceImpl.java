package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaDataDictionaryDao;
import com.example.domain.dto.StudyJavaDataDictionaryDto;
import com.example.domain.vo.StudyJavaDataDictionaryVo;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.service.StudyJavaDataDictionaryService;
import com.example.mapper.StudyJavaDataDictionaryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.StudyJavaUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author opera
* @description 针对表【study_java_data_dictionary(数据字典表)】的数据库操作Service实现
* @createDate 2025-05-24 15:50:57
*/
@Service
public class StudyJavaDataDictionaryServiceImpl extends ServiceImpl<StudyJavaDataDictionaryMapper, StudyJavaDataDictionaryDao> implements StudyJavaDataDictionaryService {
    @Resource
    private StudyJavaDataDictionaryMapper studyJavaDataDictionaryMapper;

    @Resource
    private StudyJavaUserService studyJavaUserService;
    private StudyJavaDataDictionaryDao dto2Dao(StudyJavaDataDictionaryDto studyJavaDataDictionaryDto){
        StudyJavaDataDictionaryDao studyJavaDataDictionaryDao = new StudyJavaDataDictionaryDao();
        BeanUtils.copyProperties(studyJavaDataDictionaryDto, studyJavaDataDictionaryDao);
        return studyJavaDataDictionaryDao;
    }
    @Override
    public IPage<StudyJavaDataDictionaryVo> dataDictionaryList(Page<StudyJavaDataDictionaryDto> page, StudyJavaDataDictionaryDto studyJavaDataDictionaryDto) {

        IPage<StudyJavaDataDictionaryDao> daoPage = studyJavaDataDictionaryMapper.dataDictionaryList(page,dto2Dao(studyJavaDataDictionaryDto));
        // 转换为VO对象
        IPage<StudyJavaDataDictionaryVo> voPage = new Page<>(daoPage.getCurrent(), daoPage.getSize(), daoPage.getTotal());
        List<StudyJavaDataDictionaryVo> voList = daoPage.getRecords().stream()
            .map(dao -> {
                StudyJavaDataDictionaryVo vo = new StudyJavaDataDictionaryVo();
                BeanUtils.copyProperties(dao, vo);
                return vo;
            })
            .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public Boolean addDataDictionary(StudyJavaDataDictionaryDto studyJavaDataDictionaryDto) {
        StudyJavaDataDictionaryDao studyJavaDataDictionaryDao = dto2Dao(studyJavaDataDictionaryDto);
        studyJavaDataDictionaryDao.setUpdatedTime(new Date());
        studyJavaDataDictionaryDao.setCreatedTime(new Date());
        StudyJavaUserVo studyJavaUserVo = studyJavaUserService.getUserInfo();
        studyJavaDataDictionaryDao.setCreatedBy(studyJavaUserVo.getLoginName());
        studyJavaDataDictionaryDao.setUpdatedBy(studyJavaUserVo.getLoginName());
        return studyJavaDataDictionaryMapper.addDataDictionary(studyJavaDataDictionaryDao);
    }

    @Override
    public Boolean updateDataDictionary(StudyJavaDataDictionaryDto studyJavaDataDictionaryDto) {
        StudyJavaDataDictionaryDao studyJavaDataDictionaryDao = dto2Dao(studyJavaDataDictionaryDto);
        BeanUtils.copyProperties(studyJavaDataDictionaryDto, studyJavaDataDictionaryDao);
        studyJavaDataDictionaryDao.setUpdatedTime(new Date());
        StudyJavaUserVo studyJavaUserVo = studyJavaUserService.getUserInfo();
        studyJavaDataDictionaryDao.setCreatedBy(studyJavaUserVo.getLoginName());
        studyJavaDataDictionaryDao.setUpdatedBy(studyJavaUserVo.getLoginName());
        return studyJavaDataDictionaryMapper.updateDataDictionary(studyJavaDataDictionaryDao);
    }

    @Override
    public Boolean deleteDataDictionary(Long id) {
        return studyJavaDataDictionaryMapper.deleteDataDictionary(id);
    }

    @Override
    public StudyJavaDataDictionaryVo dataDictionaryDetail(Long id) {
        StudyJavaDataDictionaryDao studyJavaDataDictionaryDao = studyJavaDataDictionaryMapper.dataDictionaryDetail(id);
        StudyJavaDataDictionaryVo studyJavaDataDictionaryVo = new StudyJavaDataDictionaryVo();
        BeanUtils.copyProperties(studyJavaDataDictionaryDao, studyJavaDataDictionaryVo);
        return studyJavaDataDictionaryVo;
    }
}




