package com.thatblackbwoy.couponservice.service;

import com.thatblackbwoy.couponservice.dto.response.ApiResponse;

public interface CouponUsageService {
    ApiResponse couponUsage(String couponCode, Long productId);
}
