package com.camdigikey.smsservice.repository;

import com.camdigikey.smsservice.model.SendSmsRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendSmsRequestRepository extends JpaRepository<SendSmsRequest, Integer> {
}
