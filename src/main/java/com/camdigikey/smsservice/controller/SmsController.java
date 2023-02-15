package com.camdigikey.smsservice.controller;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.kafka.SendSmsProducer;
import com.camdigikey.smsservice.service.SendSmsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {

  private SendSmsProducer sendSmsProducer;

  @Autowired
  public SmsController(SendSmsProducer sendSmsProducer) {
    this.sendSmsProducer = sendSmsProducer;
  }

  @PostMapping("/send-sms")
  public ResponseEntity<String> sendSms(@RequestBody SendSmsRequestDto requestDto) {
    sendSmsProducer.produceSendSmsRequestMsg(requestDto);

    return new ResponseEntity<>("SMS delivery is in progress!", HttpStatus.OK);
  }
}
