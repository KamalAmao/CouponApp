package com.thatblackbwoy.couponservice.service.impl;

import com.thatblackbwoy.couponservice.dto.ProductDto;
import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.model.Coupon;
import com.thatblackbwoy.couponservice.model.Product;
import com.thatblackbwoy.couponservice.repository.CouponRepository;
import com.thatblackbwoy.couponservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceImplTest {
    @Autowired
    private ProductServiceImpl productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CouponRepository couponRepository;
    private double getDiscountPrice(double originalPrice){
        double discount = originalPrice * 0.10;
        double discountPrice = originalPrice - discount;
        return discountPrice;
    }

    @Test
    void addProduct() {
        Product product = Product.builder().name("rice").originalPrice(4000.00).discountPrice(getDiscountPrice(4000.00)).build();
        Product expectedResponse  = Product.builder().id(1L).name("rice").originalPrice(4000.00).discountPrice(getDiscountPrice(4000.00)).build();

        ApiResponse expectedApiResponse = ApiResponse.builder()
                .message("Product successfully added")
                .data(expectedResponse)
                .build();
        when(productRepository.save(product)).thenReturn(expectedResponse);
        ApiResponse actualResponse = productService.addProduct(new ProductDto("rice", 4000.00));
        assertEquals(expectedApiResponse, actualResponse);

    }

    @Test
    void buyAtProductOriginalPrice() {
        Product expectedResponse  = Product.builder().id(1L).name("rice").originalPrice(4000.00).discountPrice(getDiscountPrice(4000.00)).build();
        ApiResponse expectedApiResponse = ApiResponse.builder()
                .message("You can get at cheaper rate using a valid coupon")
                .data(expectedResponse.getOriginalPrice())
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(expectedResponse));
        ApiResponse actualResponse = productService.buyAtProductOriginalPrice(1L);
        assertEquals(expectedApiResponse, actualResponse);
    }

    @Test
    void buyWithCoupon() {
        Product product  = Product.builder().name("rice").originalPrice(4000.00).discountPrice(getDiscountPrice(4000.00)).build();
        Coupon coupon = Coupon.builder().couponCode("ifehha9").status("Available").isUsed(false).expirationDate(LocalDateTime.now().plusSeconds(60)).product(product).build();

        Product expectedResponse  = Product.builder().id(1L).name("rice").originalPrice(4000.00).discountPrice(getDiscountPrice(4000.00)).build();

        ApiResponse expectedApiResponse = ApiResponse.builder()
                .message("Discount applied")
                .data(expectedResponse.getDiscountPrice())
                .build();

        when(couponRepository.findByCouponCode("ifehha9")).thenReturn(coupon);
        when(productRepository.findById(1L)).thenReturn(Optional.of(expectedResponse));
        ApiResponse actualResponse = productService.buyWithCoupon(1L, "ifehha9");
        assertEquals(expectedApiResponse, actualResponse);
    }
}