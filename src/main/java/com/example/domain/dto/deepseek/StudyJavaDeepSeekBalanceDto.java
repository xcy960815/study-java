package com.example.domain.dto.deepseek;

import lombok.Data;

import java.util.List;

@Data
public class StudyJavaDeepSeekBalanceDto {
    private Boolean is_available;

    private List<BalanceInfo> balance_infos;

    @Data
    static class BalanceInfo {
        private String currency;
        private String total_balance;
        private String granted_balance;
        private String topped_up_balance;
    }
}
