package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dto.StudyJavaOrderDto;
import com.example.domain.dao.StudyJavaOrderDao;
import com.example.domain.vo.StudyJavaOrderVo;

/**
* @author opera
* @description 针对表【study_java_order】的数据库操作Service
* @createDate 2025-01-18 23:36:52
*/
public interface StudyJavaOrderService {
    /**
     * 获取订单列表
     * @param page Page<StudyJavaSysUserDto>
     * @param studyJavaOrderDto StudyJavaOrderDto
     * @return IPage<StudyJavaOrderVo>
     */
    IPage<StudyJavaOrderVo> getOrderList(IPage<StudyJavaOrderDao> page, StudyJavaOrderDto studyJavaOrderDto);


    /**
     * 获取订单信息
     */

    StudyJavaOrderVo getOrderInfo(StudyJavaOrderDto studyJavaOrderDto);

    /**
     * 新建订单
     */
    Boolean insertOrder(StudyJavaOrderDto studyJavaOrderDto);

    /**
     * 更新订单
     */
    Boolean updateOrder(StudyJavaOrderDto studyJavaOrderDto);

    /**
     * 删除订单
     */
    Boolean deleteOrder(StudyJavaOrderDto studyJavaOrderDto);
}
