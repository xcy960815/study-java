package com.example.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudyJavaLoginDto extends StudyJavaUserDto {

   private String token;
}
