package com.studyjava.lab.controller;

import java.nio.file.Path;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyjava.annotation.PreAuthorize;
import com.studyjava.lab.domain.JfrRecordingInfo;
import com.studyjava.lab.domain.StartJfrRecordingRequest;
import com.studyjava.lab.service.JfrRecordingService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/lab/jfr/recordings")
@ConditionalOnExpression("${java-lab.enabled:true} and ${java-lab.jfr.enabled:true}")
@PreAuthorize("monitor:jfr:manage")
public class JfrLabController {

  private final JfrRecordingService recordingService;

  public JfrLabController(JfrRecordingService recordingService) {
    this.recordingService = recordingService;
  }

  @PostMapping
  public JfrRecordingInfo start(@Valid @RequestBody StartJfrRecordingRequest request) {
    return recordingService.start(request);
  }

  @GetMapping("/current")
  public ResponseEntity<JfrRecordingInfo> current() {
    return recordingService
        .current()
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping("/{recordingId}/stop")
  public JfrRecordingInfo stop(@PathVariable String recordingId) {
    return recordingService.stop(recordingId);
  }

  @GetMapping("/{recordingId}/download")
  public ResponseEntity<Resource> download(@PathVariable String recordingId) {
    Path path = recordingService.getDownload(recordingId);
    Resource resource = new FileSystemResource(path);
    ContentDisposition disposition =
        ContentDisposition.attachment().filename(recordingId + ".jfr").build();
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
        .body(resource);
  }

  @DeleteMapping("/{recordingId}")
  public ResponseEntity<Void> delete(@PathVariable String recordingId) {
    recordingService.delete(recordingId);
    return ResponseEntity.noContent().build();
  }
}
