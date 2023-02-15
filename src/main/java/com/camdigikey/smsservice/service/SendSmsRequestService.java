package com.camdigikey.smsservice.service;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;
import com.camdigikey.smsservice.mapper.MapStructMapper;
import com.camdigikey.smsservice.model.SendSmsRequest;
import com.camdigikey.smsservice.repository.SendSmsRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendSmsRequestService {

  private ISmsService smsSvc;

  private RetryTemplate sendSmsRetryTemplate;

  private SendSmsRequestRepository sendSmsRequestRepo;

  private MapStructMapper mapper;

  @Autowired
  public SendSmsRequestService(
      ISmsService smsSvc,
      SendSmsRequestRepository sendSmsRequestRepo,
      MapStructMapper mapper,
      RetryTemplate sendSmsRetryTemplate
  ) {
    this.smsSvc = smsSvc;
    this.sendSmsRequestRepo = sendSmsRequestRepo;
    this.mapper = mapper;
    this.sendSmsRetryTemplate = sendSmsRetryTemplate;
  }

  public void sendSmsWithRetry(SendSmsRequest request) throws SmsException {
    // request should be already created in DB
    sendSmsRetryTemplate.execute(arg0 -> {
      try {
        smsSvc.sendSms(request);
      } catch (SmsException e) {
        throw e;
      } finally {
        incrementNumAttempts(request);
      }
      return null;
    });
  }

  public SendSmsRequest saveRequest(SendSmsRequestDto requestDto) {
    SendSmsRequest request =
        mapper.sendSmsReqDtoToSendSmsReq(requestDto);

    return  sendSmsRequestRepo.save(request);
  }

  public void incrementNumAttempts(SendSmsRequest request) {
    request.setNumAttempts(request.getNumAttempts() + 1);
    sendSmsRequestRepo.save(request);
  }
}
