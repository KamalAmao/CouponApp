package com.thatblackbwoy.couponservice.service;

import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.exception.BusinessException;

public interface CouponService {
    ApiResponse generateCoupon();
//    ApiResponse generateCouponById(Long couponId);
    ApiResponse updateCouponStatus(String couponCode);
    ApiResponse reactivateExpiredCoupon(String couponCode);
    ApiResponse allocateCouponToCustomer(Long customerId) throws BusinessException;
//    void reactivateAllUnusedAndExpiredCoupon();

}
