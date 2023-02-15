package com.camdigikey.smsservice.service;

import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;
import com.camdigikey.smsservice.model.SendSmsRequest;

public interface ISmsService {

  void sendSms(SendSmsRequest request) throws SmsException;
}
