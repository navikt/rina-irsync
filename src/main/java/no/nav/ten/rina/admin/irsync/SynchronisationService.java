package no.nav.ten.rina.admin.irsync;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.ten.rina.admin.irsync.util.Auth;
import no.nav.ten.rina.ir.IRInitialDocumentWrapper;
import no.nav.ten.rina.ir.Syn002;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.SocketTimeoutException;

@Slf4j
@Service
@AllArgsConstructor
public class SynchronisationService {
  private final Auth auth;

  @Retryable( includes = {HttpClientErrorException.class, HttpServerErrorException.class, SocketTimeoutException.class},
          maxRetries = 8, delay = 1234, multiplier = 5)
  public IRInitialDocumentWrapper getInitialDocument() {
    return auth.client().get()
            .uri(uriBuilder -> uriBuilder.path("Synchronizations/IR/InitialDocument").build())
            .retrieve()
            .body(IRInitialDocumentWrapper.class);
  }

  @Retryable( includes = {HttpClientErrorException.class, HttpServerErrorException.class, SocketTimeoutException.class},
          maxRetries = 8, delay = 1234, multiplier = 5)
  public void requestIRSync(Syn002 request) {
    auth.client().put()
            .uri(uriBuilder -> uriBuilder.path("Synchronizations/IR/Document").build())
            .body(request)
            .retrieve()
            .toBodilessEntity();
  }
}
