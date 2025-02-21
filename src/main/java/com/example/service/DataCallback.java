package com.example.service;

public interface DataCallback {
    void onDataReceived(String data);
    void onComplete();
    void onError(Exception e);
}
