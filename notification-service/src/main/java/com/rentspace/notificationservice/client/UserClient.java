package com.rentspace.notificationservice.client;

import com.rentspace.core.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userservice", url = "http://localhost:8080")
public interface UserClient {
    @GetMapping("/api/v1/users/{userId}")
    UserDto getUserById(Long userId);
}
