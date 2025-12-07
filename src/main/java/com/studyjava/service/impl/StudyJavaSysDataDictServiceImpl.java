package com.studyjava.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.domain.dao.StudyJavaSysDataDictDao;
import com.studyjava.domain.dto.StudyJavaSysDataDictDto;
import com.studyjava.domain.vo.StudyJavaSysDataDictVo;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.service.StudyJavaSysDataDictService;
import com.studyjava.mapper.StudyJavaSysDataDictMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyjava.service.StudyJavaSysUserService;
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
public class StudyJavaSysDataDictServiceImpl extends ServiceImpl<StudyJavaSysDataDictMapper, StudyJavaSysDataDictDao> implements StudyJavaSysDataDictService {
    @Resource
    private StudyJavaSysDataDictMapper studyJavaSysDataDictMapper;

    @Resource
    private StudyJavaSysUserService studyJavaSysUserService;
    private StudyJavaSysDataDictDao dto2Dao(StudyJavaSysDataDictDto studyJavaSysDataDictDto){
        StudyJavaSysDataDictDao studyJavaSysDataDictDao = new StudyJavaSysDataDictDao();
        BeanUtils.copyProperties(studyJavaSysDataDictDto, studyJavaSysDataDictDao);
        return studyJavaSysDataDictDao;
    }
    @Override
    public IPage<StudyJavaSysDataDictVo> getDataDictList(Page<StudyJavaSysDataDictDto> page, StudyJavaSysDataDictDto studyJavaSysDataDictDto) {

        IPage<StudyJavaSysDataDictDao> daoPage = studyJavaSysDataDictMapper.getDataDictList(page,dto2Dao(studyJavaSysDataDictDto));
        // 转换为VO对象
        IPage<StudyJavaSysDataDictVo> voPage = new Page<>(daoPage.getCurrent(), daoPage.getSize(), daoPage.getTotal());
        List<StudyJavaSysDataDictVo> voList = daoPage.getRecords().stream()
            .map(dao -> {
                StudyJavaSysDataDictVo vo = new StudyJavaSysDataDictVo();
                BeanUtils.copyProperties(dao, vo);
                return vo;
            })
            .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public Boolean insertDataDict(StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
        StudyJavaSysDataDictDao studyJavaSysDataDictDao = dto2Dao(studyJavaSysDataDictDto);
        studyJavaSysDataDictDao.setUpdatedTime(new Date());
        studyJavaSysDataDictDao.setCreatedTime(new Date());
        StudyJavaSysUserVo studyJavaSysUserVo = studyJavaSysUserService.getUserInfo();
        studyJavaSysDataDictDao.setCreatedBy(studyJavaSysUserVo.getLoginName());
        studyJavaSysDataDictDao.setUpdatedBy(studyJavaSysUserVo.getLoginName());
        return studyJavaSysDataDictMapper.insertDataDict(studyJavaSysDataDictDao);
    }

    @Override
    public Boolean updateDataDict(StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
        StudyJavaSysDataDictDao studyJavaSysDataDictDao = dto2Dao(studyJavaSysDataDictDto);
        BeanUtils.copyProperties(studyJavaSysDataDictDto, studyJavaSysDataDictDao);
        studyJavaSysDataDictDao.setUpdatedTime(new Date());
        StudyJavaSysUserVo studyJavaSysUserVo = studyJavaSysUserService.getUserInfo();
        studyJavaSysDataDictDao.setCreatedBy(studyJavaSysUserVo.getLoginName());
        studyJavaSysDataDictDao.setUpdatedBy(studyJavaSysUserVo.getLoginName());
        return studyJavaSysDataDictMapper.updateDataDict(studyJavaSysDataDictDao);
    }

    @Override
    public Boolean deleteDataDict(Long id) {
        return studyJavaSysDataDictMapper.deleteDataDict(id);
    }

    @Override
    public StudyJavaSysDataDictVo getDataDictDetail(Long id) {
        StudyJavaSysDataDictDao studyJavaSysDataDictDao = studyJavaSysDataDictMapper.getDataDictDetail(id);
        StudyJavaSysDataDictVo studyJavaSysDataDictVo = new StudyJavaSysDataDictVo();
        BeanUtils.copyProperties(studyJavaSysDataDictDao, studyJavaSysDataDictVo);
        return studyJavaSysDataDictVo;
    }
}




