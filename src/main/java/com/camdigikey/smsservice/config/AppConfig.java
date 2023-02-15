package com.camdigikey.smsservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:application.yml")
@EnableRetry
@RefreshScope
public class AppConfig {

  @Value("${retry.maxAttempts}")
  private int maxAttempts;

  @Value("${retry.maxDelay}")
  private int maxDelay;

  @Autowired
  private ApplicationContext appContext;

  @Bean
  @RefreshScope
  public RetryTemplate sendSmsRetryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
    fixedBackOffPolicy.setBackOffPeriod(maxDelay);
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(maxAttempts);
    retryTemplate.setRetryPolicy(retryPolicy);

    SendSmsRetryListener retryListener = new SendSmsRetryListener();
    retryTemplate.registerListener(retryListener);

    return retryTemplate;
  }

  @Component
  @Slf4j
  static class SendSmsRetryListener extends RetryListenerSupport {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
                                                 RetryCallback<T, E> callback) {
      log.info("init sending sms process");

      return super.open(context, callback);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context,
                                                 RetryCallback<T, E> callback, Throwable throwable) {
      log.info("retrying to send sms again");

      super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback, Throwable throwable) {
      log.info("sending sms process ended");
      super.close(context, callback, throwable);
    }

  }
}
