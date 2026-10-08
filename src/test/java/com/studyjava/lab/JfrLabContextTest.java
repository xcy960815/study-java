package com.studyjava.lab;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.studyjava.lab.config.JavaLabProperties;
import com.studyjava.lab.controller.JfrLabController;
import com.studyjava.lab.service.JfrRecordingService;

class JfrLabContextTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner().withUserConfiguration(JfrConfiguration.class);

  @Test
  void createsServiceAndControllerWithDefaultSettings() {
    contextRunner.run(
        context -> {
          assertThat(context).hasNotFailed();
          assertThat(context).hasSingleBean(JfrRecordingService.class);
          assertThat(context).hasSingleBean(JfrLabController.class);
          assertThat(context.getBean(JfrRecordingService.class).current()).isEmpty();
        });
  }

  @Test
  void omitsJfrBeansWhenJfrIsDisabled() {
    contextRunner
        .withPropertyValues("java-lab.jfr.enabled=false")
        .run(
            context -> {
              assertThat(context).hasNotFailed();
              assertThat(context).doesNotHaveBean(JfrRecordingService.class);
              assertThat(context).doesNotHaveBean(JfrLabController.class);
            });
  }

  @Test
  void omitsJfrBeansWhenLabIsDisabled() {
    contextRunner
        .withPropertyValues("java-lab.enabled=false")
        .run(
            context -> {
              assertThat(context).hasNotFailed();
              assertThat(context).doesNotHaveBean(JfrRecordingService.class);
              assertThat(context).doesNotHaveBean(JfrLabController.class);
            });
  }

  @Configuration(proxyBeanMethods = false)
  @Import({JavaLabProperties.class, JfrRecordingService.class, JfrLabController.class})
  static class JfrConfiguration {}
}
