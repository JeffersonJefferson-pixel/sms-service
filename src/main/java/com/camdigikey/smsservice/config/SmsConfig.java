package com.camdigikey.smsservice.config;

import com.camdigikey.smsservice.service.ISmsService;
import com.camdigikey.smsservice.service.PlasgateService;
import com.camdigikey.smsservice.service.TwillioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

  @Value("${sms.provider}")
  private String provider;

  @Autowired
  private ApplicationContext appContext;

  @Bean
  public ISmsService smsSvc() {
    switch(provider) {
      case "twillio":
        return appContext.getBean(TwillioService.class);
      case "plasgate":
        return appContext.getBean(PlasgateService.class);
      default:
        return appContext.getBean(TwillioService.class);
    }
  }

}
