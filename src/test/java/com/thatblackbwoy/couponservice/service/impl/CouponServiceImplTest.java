package com.thatblackbwoy.couponservice.service.impl;

import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.model.Coupon;
import com.thatblackbwoy.couponservice.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

//import static com.thatblackbwoy.couponservice.service.impl.CouponServiceImpl.TOKEN_LENGTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CouponServiceImplTest {
    @Autowired
    private CouponServiceImpl couponService;
    @MockBean
    private CouponRepository couponRepository;
    private static final int TOKEN_LENGTH = 7;

    private String getCouponCode(){
        SecureRandom secureRandom = new SecureRandom();
        String alphaNumeric = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tokenBuilder = new StringBuilder();
        IntStream.range(0, TOKEN_LENGTH)
                .mapToObj(i -> alphaNumeric.charAt(secureRandom.nextInt(alphaNumeric.length())))
                .forEach(tokenBuilder::append);
        return tokenBuilder.toString();
    }
    private LocalDateTime getExpirationDate(){
        return LocalDateTime.now().plusMinutes(2);
    }
    @Test
    void generateCoupon() {
        Coupon coupon = Coupon.builder().couponCode(getCouponCode()).status("Available").isUsed(false).expirationDate(getExpirationDate()).build();
        Coupon expectedResponse = Coupon.builder().id(1L).couponCode(getCouponCode()).status("Available").isUsed(false).expirationDate(getExpirationDate()).build();
        ApiResponse expectedApiResponse = ApiResponse.builder()
                .message("Enjoy 10 % off anything you buy today")
                .data(expectedResponse)
                .build();

        when(couponRepository.save(coupon)).thenReturn(expectedResponse);
        ApiResponse actualResponse = couponService.generateCoupon();
        assertEquals(expectedApiResponse, actualResponse);
    }

    @Test
    void updateCouponStatus() {
        Coupon coupon = Coupon.builder().couponCode("ifehha9").status("Available").isUsed(false).expirationDate(LocalDateTime.now().plusSeconds(60)).build();
        if (coupon != null && coupon.isUsed() == false && LocalDateTime.now().isAfter(coupon.getExpirationDate())) {
            coupon.setUsed(true);
            coupon.setStatus("Expired");
        }
        Coupon expectedResponse = Coupon.builder().couponCode("ifehha9").status("Available").isUsed(false).expirationDate(LocalDateTime.now().plusSeconds(60)).build();
        //Coupon expectedResponse1 = Coupon.builder().couponCode("ifehha9").status("Expired").isUsed(true).expirationDate(LocalDateTime.now().plusSeconds(60)).build();

        ApiResponse expectedApiresponse = ApiResponse.builder()
                .message("Coupon is still available for usage")
                .data(expectedResponse)
                .build();
        when(couponRepository.findByCouponCode("ifehha9")).thenReturn(expectedResponse);
        when(couponRepository.save(coupon)).thenReturn(expectedResponse);
        ApiResponse actualResponse = couponService.updateCouponStatus("ifehha9");
        assertEquals(expectedApiresponse, actualResponse);

    }

//    @Test
//    void generateCoupon() {
//        Coupon expectedResponse = Coupon.builder().id(1L).couponCode(getCouponCode()).status("Available").isUsed(false).expirationDate(getExpirationDate()).build();
//        ApiResponse expectedApiResponse = ApiResponse.builder()
//                .message("Enjoy 10 % off anything you buy today")
//                .data(expectedResponse)
//                .build();
//        when(couponRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedResponse));
//        ApiResponse actualResponse = couponService.generateCouponById(1L);
//        assertEquals(expectedApiResponse, actualResponse);
//    }
}