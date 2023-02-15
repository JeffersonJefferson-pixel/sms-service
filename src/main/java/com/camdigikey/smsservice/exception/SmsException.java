package com.camdigikey.smsservice.exception;

public class SmsException extends RuntimeException {

  public static final String ERROR_MESSAGE =
      "SMS service is unavailable";

  public SmsException() {
    super(ERROR_MESSAGE);
  }

  public SmsException(String message) {
    super(message);
  }

  public SmsException(Throwable cause) {
    super(cause);
  }

  public SmsException(String message, Throwable cause) {
    super(message, cause);
  }
}
