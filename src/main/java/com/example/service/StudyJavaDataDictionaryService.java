package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaDataDictionaryDao;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dto.StudyJavaDataDictionaryDto;
import com.example.domain.vo.StudyJavaDataDictionaryVo;

/**
* @author opera
* @description 针对表【study_java_data_dictionary(数据字典表)】的数据库操作Service
* @createDate 2025-05-24 15:50:57
*/
public interface StudyJavaDataDictionaryService extends IService<StudyJavaDataDictionaryDao> {

    IPage<StudyJavaDataDictionaryVo> dataDictionaryList(Page<StudyJavaDataDictionaryDto> page, StudyJavaDataDictionaryDto studyJavaDataDictionaryDto);

    Boolean addDataDictionary(StudyJavaDataDictionaryDto studyJavaDataDictionaryDto);

    Boolean updateDataDictionary(StudyJavaDataDictionaryDto studyJavaDataDictionaryDto);

    Boolean deleteDataDictionary(Long id);

    StudyJavaDataDictionaryVo dataDictionaryDetail(Long id);

}
