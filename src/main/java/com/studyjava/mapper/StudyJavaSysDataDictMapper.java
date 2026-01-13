package com.studyjava.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.domain.dao.StudyJavaSysDataDictDao;
import com.studyjava.domain.dto.StudyJavaSysDataDictDto;

/**
 * @author opera
 * @description 针对表【study_java_data_dictionary(数据字典表)】的数据库操作Mapper
 * @createDate 2025-05-24 15:50:57
 */
public interface StudyJavaSysDataDictMapper extends BaseMapper<StudyJavaSysDataDictDao> {

  IPage<StudyJavaSysDataDictDao> getDataDictList(
      @Param("page") Page<StudyJavaSysDataDictDto> page,
      @Param("studyJavaSysDataDictDao") StudyJavaSysDataDictDao studyJavaSysDataDictDao);

  Boolean insertDataDict(
      @Param("studyJavaSysDataDictDao") StudyJavaSysDataDictDao studyJavaSysDataDictDao);

  Boolean updateDataDict(
      @Param("studyJavaSysDataDictDao") StudyJavaSysDataDictDao studyJavaSysDataDictDao);

  Boolean deleteDataDict(@Param("id") Long id);

  StudyJavaSysDataDictDao getDataDictDetail(@Param("id") Long id);
}
