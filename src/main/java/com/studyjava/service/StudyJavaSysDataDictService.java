package com.studyjava.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studyjava.domain.dao.StudyJavaSysDataDictDao;
import com.studyjava.domain.dto.StudyJavaSysDataDictDto;
import com.studyjava.domain.vo.StudyJavaSysDataDictVo;

/**
 * @author opera
 * @description 针对表【study_java_data_dictionary(数据字典表)】的数据库操作Service
 * @createDate 2025-05-24 15:50:57
 */
public interface StudyJavaSysDataDictService extends IService<StudyJavaSysDataDictDao> {

  IPage<StudyJavaSysDataDictVo> getDataDictList(
      Page<StudyJavaSysDataDictDto> page, StudyJavaSysDataDictDto studyJavaSysDataDictDto);

  Boolean insertDataDict(StudyJavaSysDataDictDto studyJavaSysDataDictDto);

  Boolean updateDataDict(StudyJavaSysDataDictDto studyJavaSysDataDictDto);

  Boolean deleteDataDict(Long id);

  StudyJavaSysDataDictVo getDataDictDetail(Long id);
}
