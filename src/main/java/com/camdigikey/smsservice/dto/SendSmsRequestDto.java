package com.camdigikey.smsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendSmsRequestDto {

  private String sender;
  private String receiver;
  private String message;
}
