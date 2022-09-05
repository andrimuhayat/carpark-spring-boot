package com.andri.carpark.config;

import kong.unirest.ContentType;
import kong.unirest.HeaderNames;
import kong.unirest.Unirest;
import kong.unirest.jackson.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class UnirestConfig {

  @Bean
  public void baseConfig() {
    Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false)
        .addDefaultHeader(HeaderNames.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
        .connectTimeout(180000).socketTimeout(180000).instrumentWith(requestSummary -> {
          long startNanos = System.nanoTime();
          return (responseSummary, exception) -> log.info("path: {} status: {} time in s: {}",
              requestSummary.getRawPath(), responseSummary.getStatus(),
              (System.nanoTime() - startNanos) / 1000000000);
        });
  }
}
