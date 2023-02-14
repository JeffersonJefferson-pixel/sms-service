package com.camdigikey.smsservice.service;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
@Slf4j
public class TwillioService implements ISmsService {

  @Value("${sms.twillio.account-sid}")
  private String accountSid;

  @Value("${sms.twillio.auth-token}")
  private String authToken;

  @Value("${sms.twillio.phone-number}")
  private String phoneNumber;

  public void sendSms(SendSmsRequestDto requestDto) {
    log.info("Sending SMS with Twillio");
    Twilio.init(accountSid, authToken);
    Message message = Message.creator(
            new PhoneNumber(requestDto.getReceiver()),
            new PhoneNumber(phoneNumber),
            requestDto.getMessage())
        .create();

    log.info("SMS delivery is in progress!");
  }
}
