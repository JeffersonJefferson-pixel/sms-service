package com.camdigikey.smsservice.mapper;

import com.camdigikey.smsservice.dto.PlasgateSendSmsRequestDto;
import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.model.SendSmsRequest;
import com.camdigikey.smsservice.schema.SendSmsRequestMsg;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  SendSmsRequestDto sendSmsReqMsgToSendSmsReqDto(SendSmsRequestMsg message);

  SendSmsRequestMsg sendSmsReqDtoToSendSmsReqMsg(SendSmsRequestDto requestDto);

  default PlasgateSendSmsRequestDto sendSmsReqToPlasgateSendSmsReqDto(
      SendSmsRequest request,
      String plasgateSender
  ) {
    // get rid of plus sign if exist
    long receiver = Long.parseLong(request.getReceiver());
    return PlasgateSendSmsRequestDto.builder()
        .sender(plasgateSender)
        // convert back to string
        .to(String.valueOf(receiver))
        .content(request.getMessage())
        .build();
  }

  default SendSmsRequest sendSmsReqDtoToSendSmsReq(SendSmsRequestDto requestDto) {
    return SendSmsRequest.builder()
        .sender(requestDto.getSender())
        .receiver(requestDto.getReceiver())
        .message(requestDto.getMessage())
        .requestedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .numAttempts(0)
        .build();
  }
}
