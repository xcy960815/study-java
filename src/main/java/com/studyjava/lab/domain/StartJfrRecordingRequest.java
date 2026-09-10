package com.studyjava.lab.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StartJfrRecordingRequest(
    @NotNull(message = "configuration 不能为空") JfrConfiguration configuration,
    @Min(value = 1, message = "durationSeconds 必须在 1 到 600 之间")
        @Max(value = 600, message = "durationSeconds 必须在 1 到 600 之间")
        int durationSeconds) {}
