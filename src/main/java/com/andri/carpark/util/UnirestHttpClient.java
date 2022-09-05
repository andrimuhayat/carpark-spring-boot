package com.andri.carpark.util;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UnirestHttpClient {

  private static final String HTTP_CALL_LOGGING = "URL = {} | HEADERS = {} | QUERY = {}";

  public HttpResponse<JsonNode> get(String url, Map<String, String> headers, Map<String, Object> queryString) {

    log.info(HTTP_CALL_LOGGING, url, headers, queryString);
    return Unirest.get(url).headers(headers).queryString(queryString).asJson();
  }

  public String getWithBasicAuth(
      String url,
      Map<String, String> headers,
      Map<String, Object> queryString,
      String username,
      String password) {

    log.info(HTTP_CALL_LOGGING, url, headers, queryString);
    return Unirest.get(url)
        .headers(headers)
        .basicAuth(username, password)
        .queryString(queryString)
        .asString()
        .getBody();
  }

  public HttpResponse<String> postWithEntity(
      String url, Map<String, String> headers, Map<String, Object> queryString, Object request) {

    log.info(HTTP_CALL_LOGGING, url, headers, queryString);
    return Unirest.post(url)
        .headers(headers)
        .queryString(queryString)
        .body(request)
        .asString();
  }
}
