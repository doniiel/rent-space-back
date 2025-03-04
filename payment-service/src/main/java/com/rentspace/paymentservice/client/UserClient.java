package com.rentspace.paymentservice.client;

import com.rentspace.core.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/v1/users")
public interface UserClient {
    @GetMapping("/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable Long userId);
}
