package no.nav.ten.rina.admin.irsync.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class Auth {
  @Value("${CAS_URL}")
  String casUrl;
  @Value("${CPI_URL}")
  String cpiUrl;
  @Value("${CAS_SERVICE_ID}")
  String casServiceId;
  @Value("${CPI_USERNAME}")
  String cpiUsername;
  @Value("${CPI_PASSWORD}")
  String cpiPassword;
  @Value("${CAS_TGT_HTTPS}")
  boolean casTgtHttps;

  String xauthCookie, jsessionId;

  public RestClient client() {
    try {
      log.debug("Retrieving TGT from CAS");
      var stUrl = RestClient.builder().baseUrl(casUrl).build()
              .post()
              .uri("/eessiCas/v1/tickets")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .body(String.format("username=%s&password=%s", cpiUsername, cpiPassword))
              .retrieve()
              .toEntity(String.class)
              .getHeaders()
              .getLocation()
              .toString();
      if (Boolean.TRUE.equals(casTgtHttps)) {
        log.info("CAS TGT HTTP scheme is set to HTTPS");
        stUrl = stUrl.replace("http", "https");
      }

      log.debug("Retrieving ST from CAS");
      var serviceTicket = RestClient.builder().baseUrl(stUrl).build()
              .post()
              .uri(uriBuilder -> uriBuilder.queryParam("service", casServiceId).build())
              .retrieve()
              .toEntity(String.class)
              .getBody();

      log.debug("Authenticating with CPI");
      var httpHeaders = RestClient.builder().baseUrl(cpiUrl).build()
              .get()
              .uri(String.format("/eessiRest/login/cas?ticket=%s&serviceId=%s", serviceTicket, casServiceId))
              .retrieve()
              .toEntity(String.class)
              .getHeaders();
      xauthCookie = httpHeaders.getFirst("X-Auth-Cookie");
      jsessionId = httpHeaders.getFirst("Set-Cookie");
      log.debug("X-Auth-Cookie: {} JSESSIONID: {}", xauthCookie, getJsessionCookie(jsessionId));
    } catch (NullPointerException | HttpClientErrorException e) {
      log.error("Failed to authenticate> {}", e.getMessage());
      throw e;
    }

    return RestClient.builder()
            .baseUrl(cpiUrl + "/eessiRest/")
            .defaultHeaders(httpHeaders -> {
              httpHeaders.add("Cookie", String.format("JSESSIONID=%s;XSRF-TOKEN=%s;", getJsessionCookie(jsessionId), xauthCookie));
              httpHeaders.add("X-XSRF-TOKEN", xauthCookie);
            })
            .build();
  }

  private String getJsessionCookie(String jsessionHeader) {
    var jsessionid = jsessionHeader.split(";")[0];
    return jsessionid.split("=")[1];
  }
}
