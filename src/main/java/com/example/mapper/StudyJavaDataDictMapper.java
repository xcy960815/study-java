package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysDataDictDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.dto.StudyJavaSysDataDictDto;
import org.apache.ibatis.annotations.Param;
/**
* @author opera
* @description 针对表【study_java_data_dictionary(数据字典表)】的数据库操作Mapper
* @createDate 2025-05-24 15:50:57
*/
public interface StudyJavaDataDictMapper extends BaseMapper<StudyJavaSysDataDictDao> {

    IPage<StudyJavaSysDataDictDao> dataDictionaryList(@Param("page") Page<StudyJavaSysDataDictDto> page, @Param("studyJavaDataDictionaryDao") StudyJavaSysDataDictDao studyJavaSysDataDictDao);

    Boolean addDataDictionary(@Param("studyJavaDataDictionaryDao") StudyJavaSysDataDictDao studyJavaSysDataDictDao);

    Boolean updateDataDictionary(@Param("studyJavaDataDictionaryDao") StudyJavaSysDataDictDao studyJavaSysDataDictDao);

    Boolean deleteDataDictionary(@Param("id") Long id);

    StudyJavaSysDataDictDao dataDictionaryDetail(@Param("id") Long id);

}




