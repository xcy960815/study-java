package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysDataDictDao;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dto.StudyJavaSysDataDictDto;
import com.example.domain.vo.StudyJavaSysDataDictVo;

/**
* @author opera
* @description 针对表【study_java_data_dictionary(数据字典表)】的数据库操作Service
* @createDate 2025-05-24 15:50:57
*/
public interface StudyJavaSysDataDictService extends IService<StudyJavaSysDataDictDao> {

    IPage<StudyJavaSysDataDictVo> dataDictionaryList(Page<StudyJavaSysDataDictDto> page, StudyJavaSysDataDictDto studyJavaSysDataDictDto);

    Boolean addDataDictionary(StudyJavaSysDataDictDto studyJavaSysDataDictDto);

    Boolean updateDataDictionary(StudyJavaSysDataDictDto studyJavaSysDataDictDto);

    Boolean deleteDataDictionary(Long id);

    StudyJavaSysDataDictVo dataDictionaryDetail(Long id);

}
