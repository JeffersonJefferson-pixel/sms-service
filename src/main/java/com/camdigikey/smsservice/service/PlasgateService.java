package com.camdigikey.smsservice.service;

import com.camdigikey.smsservice.dto.PlasgateSendSmsRequestDto;
import com.camdigikey.smsservice.dto.SendSmsRequestDto;
import com.camdigikey.smsservice.exception.SmsException;
import com.camdigikey.smsservice.mapper.MapStructMapper;
import com.camdigikey.smsservice.model.SendSmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PlasgateService implements ISmsService {

  @Value("${sms.plasgate.private-key}")
  private String privateKey;

  @Value("${sms.plasgate.secret-key}")
  private String secretKey;

  @Value("${sms.plasgate.sender}")
  private String sender;

  private WebClient webClient;

  private MapStructMapper mapper;

  @Autowired
  public PlasgateService(
      WebClient.Builder webClientBuilder,
      MapStructMapper mapper
  ) {
    this.webClient = webClientBuilder.baseUrl("https://cloudapi.plasgate.com").build();;
    this.mapper = mapper;
  }

  public void sendSms(SendSmsRequest request) throws SmsException {
    log.info("Sending SMS with Plasgate");
    PlasgateSendSmsRequestDto plasgateRequestDto =
        mapper.sendSmsReqToPlasgateSendSmsReqDto(request, sender);

    try {
      String resp = webClient.post()
          .uri(uriBuilder -> uriBuilder
              .path("/rest/send")
              .build()
          )
          .headers(httpHeaders -> {
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.set("X-Secret", secretKey);
          })
          .body(Mono.just(plasgateRequestDto), PlasgateSendSmsRequestDto.class)
          .retrieve()
          .bodyToMono(String.class)
          .onErrorResume(e -> Mono.error(SmsException::new))
          .block();

      log.info("Response from Plasgate: {}", resp);
    } catch (SmsException e) {
      throw e;
    }


  }

}
