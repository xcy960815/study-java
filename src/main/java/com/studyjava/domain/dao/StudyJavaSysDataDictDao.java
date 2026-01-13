package com.studyjava.domain.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 数据字典表 @TableName study_java_sys_data_dictionary */
@TableName(value = "study_java_sys_data_dictionary")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaSysDataDictDao extends BaseDao {
  /** 主键ID */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 字典类型 */
  private String dictType;

  /** 字典编码 */
  private String dictCode;

  /** 字典名称 */
  private String dictName;

  /** 字典值 */
  private String dictValue;

  /** 排序号 */
  private Integer sortOrder;

  /** 状态（0-禁用，1-启用） */
  private Integer status;
}
