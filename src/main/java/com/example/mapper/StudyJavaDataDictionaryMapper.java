package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaDataDictionaryDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.dto.StudyJavaDataDictionaryDto;
import org.apache.ibatis.annotations.Param;
/**
* @author opera
* @description 针对表【study_java_data_dictionary(数据字典表)】的数据库操作Mapper
* @createDate 2025-05-24 15:50:57
*/
public interface StudyJavaDataDictionaryMapper extends BaseMapper<StudyJavaDataDictionaryDao> {

    IPage<StudyJavaDataDictionaryDao> dataDictionaryList(@Param("page") Page<StudyJavaDataDictionaryDto> page,@Param("studyJavaDataDictionaryDao") StudyJavaDataDictionaryDao studyJavaDataDictionaryDao);

    Boolean addDataDictionary(@Param("studyJavaDataDictionaryDao") StudyJavaDataDictionaryDao studyJavaDataDictionaryDao);

    Boolean updateDataDictionary(@Param("studyJavaDataDictionaryDao") StudyJavaDataDictionaryDao studyJavaDataDictionaryDao);

    Boolean deleteDataDictionary(@Param("id") Long id);

    StudyJavaDataDictionaryDao dataDictionaryDetail(@Param("id") Long id);

}




