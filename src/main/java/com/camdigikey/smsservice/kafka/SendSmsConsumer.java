package com.camdigikey.smsservice.kafka;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;
import com.camdigikey.smsservice.mapper.MapStructMapper;
import com.camdigikey.smsservice.service.ISmsService;
import com.camdigikey.smsservice.schema.GenericReplyMsg;
import com.camdigikey.smsservice.schema.SendSmsReplyMsg;
import com.camdigikey.smsservice.schema.SendSmsRequestMsg;
import com.camdigikey.smsservice.dto.SendSmsResponseDto;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class SendSmsConsumer {

  private MapStructMapper mapper;

  private ISmsService smsSvc;

  @Getter
  private CountDownLatch latch;

  @Autowired
  public SendSmsConsumer(MapStructMapper mapper, ISmsService smsSvc) {
    this.mapper = mapper;
    this.smsSvc = smsSvc;
    this.latch = new CountDownLatch(1);
  }

  @SendTo("${kafka.topic.send-sms.reply}")
  @KafkaListener(
      topics = "${kafka.topic.send-sms.request}",
      groupId = "groupId",
      containerFactory = "genericListenerFactory"
  )
  GenericReplyMsg consumeSendSmsRequestMsg(SendSmsRequestMsg message) {
    log.info("Consuming send-sms request");
    GenericReplyMsg reply;

    SendSmsRequestDto requestDto = mapper.sendSmsReqMsgToSendSmsReqDto(message);

    SendSmsReplyMsg data = SendSmsReplyMsg.newBuilder()
        .setSender(requestDto.getSender())
        .setReceiver(requestDto.getReceiver())
        .build();

    try {
      smsSvc.sendSms(requestDto);

      reply = GenericReplyMsg.newBuilder()
          .setCode(0)
          .setData(data)
          .setMessage(SendSmsResponseDto.SUCCESS_MESSAGE)
          .build();
    } catch (SmsException e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String stackTrace = sw.toString();

      reply = GenericReplyMsg.newBuilder()
          .setCode(1)
          .setData(data)
          .setMessage(stackTrace)
          .build();
    }

    latch.countDown();

    return reply;
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
