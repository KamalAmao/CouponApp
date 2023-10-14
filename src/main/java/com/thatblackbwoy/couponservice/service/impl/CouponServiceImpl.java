package com.thatblackbwoy.couponservice.service.impl;

import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.exception.BusinessException;
import com.thatblackbwoy.couponservice.model.Coupon;
import com.thatblackbwoy.couponservice.model.Customer;
import com.thatblackbwoy.couponservice.model.Product;
import com.thatblackbwoy.couponservice.repository.CouponRepository;
import com.thatblackbwoy.couponservice.repository.CustomerRepository;
import com.thatblackbwoy.couponservice.repository.ProductRepository;
import com.thatblackbwoy.couponservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
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
    @Override
    public ApiResponse generateCoupon() {
         Coupon coupon = Coupon.builder()
                 .couponCode(getCouponCode())
                 .status("100")
                 .isUsed(false)
//                 .discountPercentage(getDiscountPercentage())
                 .expirationDate(getExpirationDate())
                 .build();
         Coupon response = couponRepository.save(coupon);
         return ApiResponse.builder()
                .message("Enjoy 10 % off anything you buy today")
                .data(response)
                .build();
    }
//        @PostConstruct
//    public void initRecord(){
//        List<Coupon> coupons = Stream.of(
//                new Coupon(1L,"uytfr4e", "100", false, LocalDateTime.now().plusMinutes(4)),
//                new Coupon(2L,"mklo90u", "100", false, LocalDateTime.now().plusMinutes(4)),
//                new Coupon(3L,"09iuytf", "100", false, LocalDateTime.now().plusMinutes(4)),
//                new Coupon(4L,"lop0csa", "100", false, LocalDateTime.now().plusMinutes(4)),
//                new Coupon(5L,"zxw2345", "100", false, LocalDateTime.now().plusMinutes(4))
//                ).collect(Collectors.toList());
//        couponRepository.saveAll(coupons);
//    }
    @Override
    public ApiResponse allocateCouponToCustomer(Long customerId) throws BusinessException {
        LocalDateTime currentTime = LocalDateTime.now();
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new BusinessException("Customer not found"));
        List<Coupon> availableCoupon = couponRepository.findByExpirationDateGreaterThanAndStatus(currentTime, "100");
        if (!availableCoupon.isEmpty()){
            Coupon allocatedCoupon = availableCoupon.get(0);
            customer.setLastRequestTime(LocalDateTime.now());
//            allocatedCoupon.setExpirationDate(customer.getLastRequestTime().plusMinutes(5));
//            couponRepository.save(allocatedCoupon);
//            customerRepository.save(customer);
            return ApiResponse.builder()
                    .message("Enjoy 10 % off anything you buy today")
                    .data(allocatedCoupon)
                    .build();
        }
        throw new BusinessException("Coupons are not available at the moment");
    }
    @Override
    public ApiResponse updateCouponStatus(String couponCode){
        Coupon coupon = couponRepository.findByCouponCode(couponCode);
        if (coupon != null && !coupon.isUsed() && LocalDateTime.now().isAfter(coupon.getExpirationDate())){
            coupon.setUsed(false);
            coupon.setStatus("102");
            Coupon response = couponRepository.save(coupon);
            return ApiResponse.builder()
                    .message("Updated successfully")
                    .data(response)
                    .build();
        }
        if (coupon != null && coupon.isUsed()){
            return ApiResponse.builder()
                    .message("Coupon has been used")
                    .data(coupon.getStatus())
                    .build();
        }
        return ApiResponse.builder()
                .message("Coupon is still available for usage")
                .data(coupon)
                .build();
    }
    @Override
    public ApiResponse reactivateExpiredCoupon(String couponCode) {
        Coupon coupon = couponRepository.findByCouponCode(couponCode);
        if (coupon != null && !coupon.isUsed() && LocalDateTime.now().isAfter(coupon.getExpirationDate()) && coupon.getStatus().equals("Expired")){
            coupon.setUsed(false);
            coupon.setStatus("100");
            coupon.setExpirationDate(getExpirationDate());
            Coupon response = couponRepository.save(coupon);
            return ApiResponse.builder()
                    .message("unused coupons is now available for usage")
                    .data(response)
                    .build();
        }
        if (coupon != null && coupon.isUsed()){
            return ApiResponse.builder()
                    .message("Coupon has been used and cannot be reactivated")
                    .data(coupon.getStatus())
                    .build();
        }
        return ApiResponse.builder()
                .message("Coupon not used")
                .data(coupon.getStatus())
                .build();
    }
}
