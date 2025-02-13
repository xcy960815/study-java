package com.example.domain.dto.ollama;

import lombok.Data;
import java.util.List;

@Data
public class StudyJavaOllamaModelsDto {
   private String object;

   private List<Model> data;

   @Data
   public static class Model {
      private String id;
      private String object;
      private int created;
      private String owned_by;
   }
}
