package com.example.service;

import com.example.domain.StudyJavaUser;
import com.example.dto.StudyJavaUserDTO;
import java.util.List;

public interface StudyJavaUserService {

    List<StudyJavaUser> getAllUsers();

    List<StudyJavaUserDTO> getAllUsersDTO();

    String getError();

}
