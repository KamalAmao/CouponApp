package com.thatblackbwoy.couponservice.repository;

import com.thatblackbwoy.couponservice.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCouponCode(String couponCode);
    List<Coupon> findByExpirationDateGreaterThanAndStatus(LocalDateTime localDateTime, String status);
//    Coupon findByUsedFalseAndExpirationDateBefore(LocalDateTime currentTime);
//    List<Coupon> findByUsedFalseAndExpirationDateBefore(LocalDateTime localDateTime);
//    List<Coupon> findByExpirationDateBeforeAndUsedFalse(LocalDateTime localDateTime);
}
