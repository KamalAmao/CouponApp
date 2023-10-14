package com.thatblackbwoy.couponservice.repository;

import com.thatblackbwoy.couponservice.model.Coupon;
import com.thatblackbwoy.couponservice.model.CouponUsage;
import com.thatblackbwoy.couponservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    CouponUsage existsByCustomerIdAndDateUsedAfter(Long customerId, LocalDateTime localDateTime);
    CouponUsage findByCustomerIdAndDateUsedAfter(Long customerId, LocalDateTime localDateTime);
}
