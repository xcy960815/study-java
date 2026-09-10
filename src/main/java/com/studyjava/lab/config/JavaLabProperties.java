package com.studyjava.lab.config;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "java-lab")
public class JavaLabProperties {

  private boolean enabled = true;
  private boolean dangerousDemoEnabled = false;
  private Jfr jfr = new Jfr();

  @Getter
  @Setter
  public static class Jfr {
    private boolean enabled = true;
    private Path directory = Path.of("jfr-recordings");
    private int maxDurationSeconds = 600;
    private int maxSizeMb = 500;
  }
}
