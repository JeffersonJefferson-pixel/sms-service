package com.camdigikey.smsservice.kafka;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.schema.SendSmsRequestMsg;
import com.camdigikey.smsservice.schema.GenericReplyMsg;
import com.camdigikey.smsservice.schema.SendSmsReplyMsg;

import com.camdigikey.smsservice.mapper.MapStructMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class SendSmsProducer {

  @Value("${kafka.topic.send-sms.request}")
  private String topic;

  private KafkaTemplate genericKafkaTemplate;

  private MapStructMapper mapper;
  @Getter
  private CountDownLatch latch;



  @Autowired
  public SendSmsProducer(KafkaTemplate genericKafkaTemplate, MapStructMapper mapper) {
    this.genericKafkaTemplate = genericKafkaTemplate;
    this.mapper = mapper;
    this.latch = new CountDownLatch(1);
  }

  public void produceSendSmsRequestMsg(SendSmsRequestDto requestDto) {
    log.info("Producing send-sms request to {}", topic);
    SendSmsRequestMsg sendSmsMsg = mapper.sendSmsReqDtoToSendSmsReqMsg(requestDto);

    genericKafkaTemplate.send(topic, sendSmsMsg);
  }

  @KafkaListener(
      topics = "${kafka.topic.send-sms.reply}",
      groupId = "groupId",
      containerFactory = "genericListenerFactory"
  )
  public void listenForSendSmsReplyMsg(GenericReplyMsg reply) {
    log.debug("send-sms reply received");
    log.debug(reply.toString());
    SendSmsReplyMsg data = reply.getData();
    log.debug(data.toString());

    latch.countDown();
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
