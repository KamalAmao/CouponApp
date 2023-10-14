package com.thatblackbwoy.couponservice.repository;

import com.thatblackbwoy.couponservice.model.BlockSecondUser;
import com.thatblackbwoy.couponservice.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockSecondUserRepository extends JpaRepository<BlockSecondUser, Long> {
    BlockSecondUser findByCoupon(Coupon coupon);
}
