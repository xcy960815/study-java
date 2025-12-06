package com.studyjava.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dto.StudyJavaOrderDto;
import com.studyjava.domain.vo.StudyJavaOrderVo;
import com.studyjava.service.StudyJavaOrderService;
import com.studyjava.utils.PageResult;
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
     * @return PageResult<StudyJavaOrderVo>
     */
    @GetMapping("/getOrderList")
    public PageResult<StudyJavaOrderVo> getOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ModelAttribute StudyJavaOrderDto studyJavaOrderDto
    ) {
        IPage<StudyJavaOrderVo> orderVoPage = studyJavaOrderService.getOrderList(startPage(pageNum, pageSize), studyJavaOrderDto);
        return PageResult.of(orderVoPage.getRecords(), orderVoPage.getTotal());
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return StudyJavaOrderVo
     */
    @GetMapping("/getOrderInfo")
    public StudyJavaOrderVo getOrderInfo(@RequestParam("id") Long orderId) {
        StudyJavaOrderDto orderDto =  new StudyJavaOrderDto();
        orderDto.setOrderId(orderId);
        return studyJavaOrderService.getOrderInfo(orderDto);
    }

    /**
     * 新建订单
     */
    @PostMapping("/insertOrder")
    public Boolean insertOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
        return studyJavaOrderService.insertOrder(orderDto);
    }

    /**
     * 更新订单状态
     * @param orderDto 订单DTO
     * @return Boolean
     */
    @PutMapping("/updateOrder")
    public Boolean updateOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
        return studyJavaOrderService.updateOrder(orderDto);
    }

    /**
     * 删除订单（逻辑删除）
     * @param orderId 订单ID
     * @return Boolean
     */
    @DeleteMapping("/deleteOrder")
    public Boolean deleteOrder(@RequestParam("id") Long orderId) {
        StudyJavaOrderDto orderDto = new StudyJavaOrderDto();
        orderDto.setOrderId(orderId);
        return studyJavaOrderService.deleteOrder(orderDto);
    }

}
