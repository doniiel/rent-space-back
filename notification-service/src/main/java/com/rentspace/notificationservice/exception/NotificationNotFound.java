package com.rentspace.notificationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotificationNotFound extends RuntimeException {
  public NotificationNotFound(String entity, String field, Object value) {
    super(String.format("%s with %s '%s' not found.", entity, field, value));
  }
  public NotificationNotFound(String message) {
    super(message);
  }
}
