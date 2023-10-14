package com.thatblackbwoy.couponservice.controller;

import com.thatblackbwoy.couponservice.dto.CustomerDto;
import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> addCustomer(@RequestBody CustomerDto customerDto){
        try{
            return ResponseEntity.ok().body(customerService.addCustomer(customerDto));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().message("Invalid input").build());
        }
    }
    @GetMapping("/use-coupon/{customerId}")
    public ResponseEntity<ApiResponse> useCoupon(@PathVariable Long customerId, @RequestParam String couponCode){
        try{
            return ResponseEntity.ok().body(customerService.useCoupon(customerId, couponCode));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
}
