package com.studyjava.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class StudyJavaSysDataDictDto {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型
     */
    @JsonProperty("dictType")
    private String dictType;

    /**
     * 字典编码
     */
    @JsonProperty("dictCode")
    private String dictCode;

    /**
     * 字典名称
     */
    @JsonProperty("dictName")
    private String dictName;

    /**
     * 字典值
     */
    @JsonProperty("dictValue")
    private String dictValue;

    /**
     * 排序号
     */
    @JsonProperty("sortOrder")
    private Integer sortOrder;

    /**
     * 状态（0-禁用，1-启用）
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 创建人
     */
    @JsonProperty("createdBy")
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonProperty("createdTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 更新人
     */
    @JsonProperty("updatedBy")
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonProperty("updatedTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
