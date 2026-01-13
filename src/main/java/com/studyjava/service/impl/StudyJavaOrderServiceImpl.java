package com.studyjava.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.domain.dao.StudyJavaOrderDao;
import com.studyjava.domain.dto.StudyJavaOrderDto;
import com.studyjava.domain.vo.StudyJavaOrderVo;
import com.studyjava.mapper.StudyJavaOrderMapper;
import com.studyjava.service.StudyJavaOrderService;

import jakarta.annotation.Resource;

/**
 * @author opera
 * @description 针对表【study_java_order】的数据库操作Service实现
 * @createDate 2025-01-18 23:36:52
 */
@Service
public class StudyJavaOrderServiceImpl implements StudyJavaOrderService {
  @Resource private StudyJavaOrderMapper studyJavaOrderMapper;

  /**
   * 获取订单列表
   *
   * @param page Page<StudyJavaOrderDao>
   * @param studyJavaOrderDto StudyJavaOrderDto
   * @return IPage<StudyJavaOrderVo>
   */
  @Override
  public IPage<StudyJavaOrderVo> getOrderList(
      IPage<StudyJavaOrderDao> page, StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    IPage<StudyJavaOrderDao> studyJavaOrderResponseDao =
        studyJavaOrderMapper.getOrderList(page, studyJavaOrderDao);
    // 转换为 VO 并组装新分页对象
    List<StudyJavaOrderVo> voList =
        studyJavaOrderResponseDao.getRecords().stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    IPage<StudyJavaOrderVo> voPage =
        new Page<>(
            studyJavaOrderResponseDao.getCurrent(),
            studyJavaOrderResponseDao.getSize(),
            studyJavaOrderResponseDao.getTotal());
    voPage.setRecords(voList);
    return voPage;
  }

  /** dao 转 vo */
  private StudyJavaOrderVo convertToVo(StudyJavaOrderDao studyJavaOrderDao) {
    if (studyJavaOrderDao == null) {
      return null;
    }
    StudyJavaOrderVo studyJavaOrderVo = new StudyJavaOrderVo();
    BeanUtils.copyProperties(studyJavaOrderDao, studyJavaOrderVo);
    return studyJavaOrderVo;
  }

  /** dto 转 dao */
  private StudyJavaOrderDao convertToDao(StudyJavaOrderDto studyJavaOrderDto) {
    if (studyJavaOrderDto == null) {
      return null;
    }
    StudyJavaOrderDao studyJavaOrderDao = new StudyJavaOrderDao();
    BeanUtils.copyProperties(studyJavaOrderDto, studyJavaOrderDao);
    return studyJavaOrderDao;
  }

  /** 获取订单信息 */
  @Override
  public StudyJavaOrderVo getOrderInfo(StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    StudyJavaOrderDao studyJavaOrderResponseDao =
        studyJavaOrderMapper.getOrderInfo(studyJavaOrderDao);
    return convertToVo(studyJavaOrderResponseDao);
  }

  /** 新建订单 */
  @Override
  public Boolean insertOrder(StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    return studyJavaOrderMapper.insertOrder(studyJavaOrderDao);
  }

  /** 更新订单 */
  @Override
  public Boolean updateOrder(StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    return studyJavaOrderMapper.updateOrder(studyJavaOrderDao);
  }

  /** 删除订单 */
  @Override
  public Boolean deleteOrder(StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    return studyJavaOrderMapper.deleteOrder(studyJavaOrderDao);
  }

  @Override
  public Map<String, Object> getDailyStats(LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return studyJavaOrderMapper.getDailyStats(
        startOfDay.format(formatter), endOfDay.format(formatter));
  }
}
