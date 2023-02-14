package com.camdigikey.smsservice.mapper;

import com.camdigikey.smsservice.dto.PlasgateSendSmsRequestDto;
import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.schema.SendSmsRequestMsg;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  SendSmsRequestDto sendSmsReqMsgToSendSmsReqDto(SendSmsRequestMsg message);

  SendSmsRequestMsg sendSmsReqDtoToSendSmsReqMsg(SendSmsRequestDto requestDto);

  default PlasgateSendSmsRequestDto sendSmsReqDtoToPlasgateSendSmsReqDto(SendSmsRequestDto requestDto, String plasgateSender) {
    // get rid of plus sign if exist
    long receiver = Long.parseLong(requestDto.getReceiver());
    return PlasgateSendSmsRequestDto.builder()
        .sender(plasgateSender)
        // convert back to string
        .to(String.valueOf(receiver))
        .content(requestDto.getMessage())
        .build();
  }
}
