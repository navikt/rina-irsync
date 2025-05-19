package no.nav.ten.rina.admin.irsync.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingRetryListener implements RetryListener {
  
  @Override
  public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
    // leave this empty to avoid logging for every method invocation start.
    return true;
  }
  
  @Override
  public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    if (context.getRetryCount() > 0) {
      log.info("Method {} retried {} times", context.getAttribute("context.name"), context.getRetryCount());
    }
  }
  
  @Override
  public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    log.info("Retry attempt #{} for method {} due to exception: {}",
      context.getRetryCount(), context.getAttribute("context.name"), throwable.getMessage());
  }
}

