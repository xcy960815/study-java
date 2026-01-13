package com.studyjava.controller.monitor;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyjava.controller.BaseController;
import com.studyjava.service.StudyJavaOrderService;

@RestController
@RequestMapping("/monitor/report")
public class StudyJavaReportController extends BaseController {

  @Autowired private StudyJavaOrderService orderService;

  /** 获取昨日经营数据 */
  @GetMapping("/daily")
  public Map<String, Object> getDailyReport() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    return orderService.getDailyStats(yesterday);
  }
}
