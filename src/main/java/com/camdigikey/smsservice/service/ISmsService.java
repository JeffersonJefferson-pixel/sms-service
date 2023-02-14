package com.camdigikey.smsservice.service;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;

public interface ISmsService {

  void sendSms(SendSmsRequestDto requestDto) throws SmsException;
}
