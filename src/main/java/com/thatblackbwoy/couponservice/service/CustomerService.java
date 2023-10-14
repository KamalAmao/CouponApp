package com.thatblackbwoy.couponservice.service;

import com.thatblackbwoy.couponservice.dto.CustomerDto;
import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.exception.BusinessException;

public interface CustomerService {
    ApiResponse addCustomer(CustomerDto customerDto);
//    ApiResponse generateCoupon(Long customerId);
    ApiResponse useCoupon(Long customerId, String couponCode) throws BusinessException;
}
