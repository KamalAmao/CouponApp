package com.thatblackbwoy.couponservice.service.impl;

import com.thatblackbwoy.couponservice.dto.ProductDto;
import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.model.Coupon;
import com.thatblackbwoy.couponservice.model.Product;
import com.thatblackbwoy.couponservice.repository.CouponRepository;
import com.thatblackbwoy.couponservice.repository.ProductRepository;
import com.thatblackbwoy.couponservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private static final int TOKEN_LENGTH = 7;

    private double getDiscountPrice(double originalPrice){
        double discount = originalPrice * 0.10;
        double discountPrice = originalPrice - discount;
        return discountPrice;
    }
    @Override
    public ApiResponse addProduct(ProductDto productDto) {
//        Coupon coupon = couponRepository.findById(couponId).orElseThrow(()-> new RuntimeException());
        Product product = Product.builder()
                .name(productDto.getName())
                .originalPrice(productDto.getOriginalPrice())
                .discountPrice(getDiscountPrice(productDto.getOriginalPrice()))
//                .coupon(coupon)
                .build();
        Product response = productRepository.save(product);
        log.info("Product added successfully {}", response);
        return ApiResponse.builder()
                .message("Product successfully added")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse buyAtProductOriginalPrice(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException());
        return ApiResponse.builder()
                .message("You can get at cheaper rate using a valid coupon")
                .data(product.getOriginalPrice())
                .build();
    }

    @Override
    public ApiResponse buyWithCoupon(Long productId, String couponCode) {
        Coupon coupon = couponRepository.findByCouponCode(couponCode);
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException());
        //LocalDateTime localDateTime = LocalDateTime.now();
        if (LocalDateTime.now().isBefore(coupon.getExpirationDate())){
            coupon.setUsed(true);
            coupon.setStatus("Used");
//            product.setCoupon(coupon);
//            coupon.setProduct(product);
            couponRepository.save(coupon);
            //productRepository.save(product);
            return ApiResponse.builder()
                    .message("Discount applied")
                    .data(product.getDiscountPrice())
                    .build();
        }
        return buyAtProductOriginalPrice(product.getId());
    }

}
