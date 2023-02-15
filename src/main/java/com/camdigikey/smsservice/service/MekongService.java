package com.camdigikey.smsservice.service;


import com.camdigikey.smsservice.dto.PlasgateSendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;
import com.camdigikey.smsservice.mapper.MapStructMapper;
import com.camdigikey.smsservice.model.SendSmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MekongService implements ISmsService {

  @Value("${sms.mekong.username}")
  private String username;

  @Value("${sms.mekong.pass}")
  private String pass;

  private WebClient webClient;

  private MapStructMapper mapper;

  public MekongService(
      WebClient.Builder webClientBuilder,
      MapStructMapper mapper
  ) {
    this.webClient = webClientBuilder.baseUrl("https://sandbox.mekongsms.com").build();;
    this.mapper = mapper;
  }

  public void sendSms(SendSmsRequest request) throws SmsException {
    log.info("Sending SMS with Mekong");

    try {
      String resp = webClient.post()
          .uri(uriBuilder -> uriBuilder
              .path("/api/postsms.aspx")
              .queryParam("username", username)
              .queryParam("pass", pass)
              .queryParam("sender", request.getSender())
              .queryParam("gsm", request.getReceiver())
              .build()
          )
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
          .retrieve()
          .bodyToMono(String.class)
          .onErrorResume(e -> Mono.error(SmsException::new))
          .block();

      log.info("Response from Mekong: {}", resp);
    } catch (SmsException e) {
      throw e;
    }
  }
}
