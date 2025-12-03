package com.studyjava.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dto.StudyJavaOrderDto;
import com.studyjava.domain.vo.StudyJavaOrderVo;
import com.studyjava.service.StudyJavaOrderService;
import com.studyjava.utils.ResponseGenerator;
import com.studyjava.utils.ResponseListResult;
import com.studyjava.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


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
     * 获取用户订单列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 订单列表
     */
    @GetMapping("/getOrderList")
    public ResponseListResult<StudyJavaOrderVo> getOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ModelAttribute StudyJavaOrderDto studyJavaOrderDto
    ) {
        IPage<StudyJavaOrderVo> orderVoPage = studyJavaOrderService.getOrderList(startPage(pageNum, pageSize), studyJavaOrderDto);
        return ResponseGenerator.generateListResult(orderVoPage.getRecords(), orderVoPage.getTotal());
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单VO
     */
    @GetMapping("/getOrderInfo")
    public ResponseResult<StudyJavaOrderVo> getOrderInfo(@RequestParam("id") Long orderId) {
        StudyJavaOrderDto orderDto =  new StudyJavaOrderDto();
        orderDto.setOrderId(orderId);
        return ResponseGenerator.generateSuccessResult(studyJavaOrderService.getOrderInfo(orderDto));
    }

    /**
     * 新建订单
     */
    @PostMapping("/insertOrder")
    public ResponseResult<Boolean> insertOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaOrderService.insertOrder(orderDto));
    }

    /**
     * 更新订单状态
     * @param orderDto 订单DTO
     * @return 更新结果
     */
    @PutMapping("/updateOrder")
    public ResponseResult<Boolean> updateOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaOrderService.updateOrder(orderDto));
    }

    /**
     * 删除订单（逻辑删除）
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/deleteOrder")
    public ResponseResult<Boolean> deleteOrder(@RequestParam("id") Long orderId) {
        StudyJavaOrderDto orderDto = new StudyJavaOrderDto();
        orderDto.setOrderId(orderId);
        return ResponseGenerator.generateSuccessResult(studyJavaOrderService.deleteOrder(orderDto));
    }

}
