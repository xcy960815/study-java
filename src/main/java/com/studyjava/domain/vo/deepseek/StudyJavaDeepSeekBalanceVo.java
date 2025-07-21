package com.studyjava.domain.vo.deepseek;

import lombok.Data;

import java.util.List;

/**
 * DeepSeek AI 账户余额响应
 */
@Data
public class StudyJavaDeepSeekBalanceVo {
    private Boolean is_available;

    private List<BalanceInfo> balance_infos;

    @Data
    public static class BalanceInfo {
        private String currency;
        private String total_balance;
        private String granted_balance;
        private String topped_up_balance;
    }
}
