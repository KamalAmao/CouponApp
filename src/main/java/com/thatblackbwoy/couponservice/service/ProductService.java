package com.thatblackbwoy.couponservice.service;

import com.thatblackbwoy.couponservice.dto.ProductDto;
import com.thatblackbwoy.couponservice.dto.response.ApiResponse;

public interface ProductService {
    ApiResponse addProduct(ProductDto productDto);
//    ProductResponse buyProduct(String couponCode, Long productId);
    ApiResponse buyAtProductOriginalPrice(Long productId);
    ApiResponse buyWithCoupon(Long productId, String couponCode);
}
