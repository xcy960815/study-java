package com.studyjava.task;

import com.studyjava.service.StudyJavaOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DailyReportTask {

    @Autowired
    private StudyJavaOrderService orderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 每天凌晨 2 点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void generateDailyReport() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String lockKey = "task:daily_report:" + yesterday.toString();

        // 尝试获取分布式锁，设置过期时间为 1 天，防止死锁
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", 1, TimeUnit.DAYS);

        if (Boolean.TRUE.equals(acquired)) {
            log.info("开始执行每日经营报表统计任务，日期：{}", yesterday);
            try {
                Map<String, Object> stats = orderService.getDailyStats(yesterday);
                log.info("统计完成！日期：{}，总订单数：{}，总销售额：{}", 
                        yesterday, stats.get("totalOrders"), stats.get("totalRevenue"));
                
                // 在这里可以添加发送邮件或保存到数据库的逻辑
                
            } catch (Exception e) {
                log.error("每日报表统计任务执行失败", e);
                // 可以在这里添加告警逻辑
                // 任务失败是否删除锁取决于业务，如果希望重试则删除锁，如果不希望重试则保留
            }
        } else {
            log.info("每日报表统计任务已被其他实例执行，本实例跳过。日期：{}", yesterday);
        }
    }
}
