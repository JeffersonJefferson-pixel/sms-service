package com.camdigikey.smsservice;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;
import com.camdigikey.smsservice.model.SendSmsRequest;
import com.camdigikey.smsservice.service.ISmsService;
import com.camdigikey.smsservice.service.SendSmsRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext
public class SendSmsRetryUnitTest {

  @MockBean
  @Qualifier("smsSvc")
  private ISmsService smsSvc;

  @Autowired
  private SendSmsRequestService sendSmsRequestSvc;

  @Test
  public void givenSmsFail_thenTryThreeTimes() throws SmsException {
    doThrow(new SmsException()).when(smsSvc).sendSms(any(SendSmsRequest.class));

    SendSmsRequestDto requestDto = SendSmsRequestDto.builder()
        .sender("+855123456789")
        .receiver("+855123456789")
        .message("testing!")
        .build();

    SendSmsRequest request = sendSmsRequestSvc.saveRequest(requestDto);

    try {
      sendSmsRequestSvc.sendSmsWithRetry(request);
      fail();
    } catch (SmsException e) {
      assertEquals(3, request.getNumAttempts());
    }
  }

  @Test
  public void givenSmsSucceeds_thenTryOnlyOnce() throws SmsException {
    doNothing().when(smsSvc).sendSms(any(SendSmsRequest.class));

    SendSmsRequestDto requestDto = SendSmsRequestDto.builder()
        .sender("+855123456789")
        .receiver("+855123456789")
        .message("testing!")
        .build();

    SendSmsRequest request = sendSmsRequestSvc.saveRequest(requestDto);
    sendSmsRequestSvc.sendSmsWithRetry(request);

    assertEquals(1, request.getNumAttempts());
  }
}

