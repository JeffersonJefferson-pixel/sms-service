package com.camdigikey.smsservice;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.kafka.SendSmsProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SmsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmsServiceApplication.class, args);
  }


//  @Bean
//	@Profile("!test")
//  CommandLineRunner commandLineRunner(SendSmsProducer sendSmsProducer) {
//		return args -> {
//      SendSmsRequestDto requestDto = SendSmsRequestDto.builder()
//          .sender("+19035009778")
//          .receiver("+85561601260")
//          .message("testing!")
//          .build();
//
//      sendSmsProducer.produceSendSmsRequestMsg(requestDto);
//		};
//	}
}
