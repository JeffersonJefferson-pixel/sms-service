package com.camdigikey.smsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlasgateSendSmsRequestDto {

  private String sender;
  private String to;
  private String content;
}
