package com.thatblackbwoy.couponservice.controller;

import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/coupon")
    public ResponseEntity<ApiResponse> generateCoupon(){
        try{
            return ResponseEntity.ok().body(couponService.generateCoupon());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().message("Invalid input").build());
        }
    }
    @GetMapping("/coupon/{customerId}")
    public ResponseEntity<ApiResponse> allocateCouponToCustomer(@PathVariable Long customerId){
        try{
            return ResponseEntity.ok().body(couponService.allocateCouponToCustomer(customerId));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @PostMapping("/update-status")
    public ResponseEntity<ApiResponse> UpdateCouponStatus(@RequestParam String couponCode){
        try{
            return ResponseEntity.ok().body(couponService.updateCouponStatus(couponCode));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().message("Invalid input").build());
        }
    }
    @PostMapping("/reactivate")
    public ResponseEntity<ApiResponse> reactivateExpiredCoupon(@RequestParam String couponCode){
        try{
            return ResponseEntity.ok().body(couponService.reactivateExpiredCoupon(couponCode));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().message("Invalid input").build());
        }
    }
}
