package com.example.service;

import com.example.domain.StudyJavaUser;

import java.util.List;

public interface StudyJavaUserService {

    List<StudyJavaUser> getAllUsers();

    String getError();

}
