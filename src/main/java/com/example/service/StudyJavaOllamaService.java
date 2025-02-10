package com.example.service;

import java.util.List;

public interface StudyJavaOllamaService {

        public String buildRequestUrl(String url);

        public void generate();
        public void generateStream();
        public void models();
        public String version();
        public List<Object> tags();
        public void delete();
}
