package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaOrderDao;
import com.example.domain.dto.StudyJavaOrderDto;
import com.example.domain.vo.StudyJavaOrderVo;
import com.example.service.StudyJavaOrderService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class StudyJavaOrderController extends BaseController {

    @Resource
    private StudyJavaOrderService studyJavaOrderService;

    /**
     * 创建订单
     * @param orderDto 订单DTO
     * @return 订单VO
     */
    @PostMapping("/createOrder")
    public ResponseResult<StudyJavaOrderVo> createOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
        return null;
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单VO
     */
    @GetMapping("/detail/{orderId}")
    public ResponseResult<StudyJavaOrderVo> getOrderDetail(@PathVariable("orderId") Long orderId) {
        return null;
    }

    /**
     * 获取用户订单列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 订单列表
     */
    @GetMapping("/list/{userId}")
    public ResponseResult<Page<StudyJavaOrderVo>> getOrderList(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<StudyJavaOrderDao> pageParam = new Page<>(page, size);
        QueryWrapper<StudyJavaOrderDao> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("create_time");

        Page<StudyJavaOrderDao> orderDaoPage = studyJavaOrderService.page(pageParam, queryWrapper);

        // 转换为VO
        Page<StudyJavaOrderVo> orderVoPage = new Page<>(orderDaoPage.getCurrent(), orderDaoPage.getSize(), orderDaoPage.getTotal());
        List<StudyJavaOrderVo> orderVoList = new ArrayList<>();

        for (StudyJavaOrderDao orderDao : orderDaoPage.getRecords()) {
            StudyJavaOrderVo orderVo = new StudyJavaOrderVo();
            BeanUtils.copyProperties(orderDao, orderVo);
            orderVoList.add(orderVo);
        }

        orderVoPage.setRecords(orderVoList);
        return ResponseGenerator.generateSuccessResult(orderVoPage);
    }

    /**
     * 更新订单状态
     * @param orderDto 订单DTO
     * @return 更新结果
     */
    @PutMapping("/updateOrder")
    public ResponseResult<Boolean> updateOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
        return null;
    }

    /**
     * 删除订单（逻辑删除）
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseResult<Boolean> deleteOrder(@PathVariable("orderId") Long orderId) {
        return null;
    }

}
