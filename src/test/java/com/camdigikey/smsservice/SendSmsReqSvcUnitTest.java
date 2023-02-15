package com.camdigikey.smsservice;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.model.SendSmsRequest;
import com.camdigikey.smsservice.repository.SendSmsRequestRepository;
import com.camdigikey.smsservice.service.SendSmsRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext
public class SendSmsReqSvcUnitTest {

  @Autowired
  private SendSmsRequestService sendSmsReqSvc;

  @Autowired
  private SendSmsRequestRepository sendSmsReqRepo;

  @Test
  public void givenSaveSendEmailRequest_thenPersistInDb() {
    SendSmsRequestDto requestDto = SendSmsRequestDto.builder()
        .sender("+855123456789")
        .receiver("+855123456789")
        .message("testing!")
        .build();

    SendSmsRequest request =  sendSmsReqSvc.saveRequest(requestDto);
    SendSmsRequest foundReq = sendSmsReqRepo.findById(request.getId()).orElseThrow();

    assertEquals(request, foundReq);
  }

  @Test
  public void givenIncrementNumAttempt_thenNumAttemptsIncrease() {
    SendSmsRequestDto requestDto = SendSmsRequestDto.builder()
        .sender("+855123456789")
        .receiver("+855123456789")
        .message("testing!")
        .build();

    SendSmsRequest request =  sendSmsReqSvc.saveRequest(requestDto);
    SendSmsRequest foundReq0 = sendSmsReqRepo.findById(request.getId()).orElseThrow();
    assertEquals(0, foundReq0.getNumAttempts());

    sendSmsReqSvc.incrementNumAttempts(request);
    SendSmsRequest foundReq1 = sendSmsReqRepo.findById(request.getId()).orElseThrow();
    assertEquals(1, foundReq1.getNumAttempts());

    sendSmsReqSvc.incrementNumAttempts(request);
    SendSmsRequest foundReq2 = sendSmsReqRepo.findById(request.getId()).orElseThrow();
    assertEquals(2, foundReq2.getNumAttempts());
  }
}
