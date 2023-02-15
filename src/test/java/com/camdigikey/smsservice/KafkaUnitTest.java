package com.camdigikey.smsservice;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;
import com.camdigikey.smsservice.kafka.SendSmsConsumer;
import com.camdigikey.smsservice.kafka.SendSmsProducer;
import com.camdigikey.smsservice.model.SendSmsRequest;
import com.camdigikey.smsservice.service.ISmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092",
        "port=9092"
    }
)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaUnitTest {

  @MockBean
  @Qualifier("smsSvc")
  private ISmsService smsSvc;

  @Autowired
  private SendSmsProducer sendSmsProducer;
  @Autowired
  private SendSmsConsumer sendSmsConsumer;

  @BeforeEach
  void setup() {
    sendSmsConsumer.resetLatch();
    sendSmsProducer.resetLatch();
  }

  @Test
  public void givenEmbeddedKafkaBroker_whenProduceSendEmail_thenConsumeSendEmail() throws InterruptedException, SmsException {
    doNothing().when(smsSvc).sendSms(any(SendSmsRequest.class));

    SendSmsRequestDto requestDto = SendSmsRequestDto.builder()
        .sender("+855123456789")
        .receiver("+855123456789")
        .message("testing!")
        .build();

    sendSmsProducer.produceSendSmsRequestMsg(requestDto);

    boolean messageConsumed =
        sendSmsConsumer.getLatch().await(10, TimeUnit.SECONDS);
    boolean replyConsumed =
        sendSmsProducer.getLatch().await(10, TimeUnit.SECONDS);

    assertTrue(messageConsumed);
    assertTrue(replyConsumed);
  }
}
