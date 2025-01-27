package com.example.domain.enums;

public enum ResponseResultEnum implements BaseEnum {
    SUCCESS {
        public int getCode() {
            return 200;
        }
        public String getMessage() {
            return "SUCCESS";
        }
    },
    ERROR {
        public int getCode() {
            return 500;
        }
        public String getMessage() {
            return "Error";
        }
    }
}
