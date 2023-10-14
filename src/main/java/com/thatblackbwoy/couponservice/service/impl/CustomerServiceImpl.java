package com.thatblackbwoy.couponservice.service.impl;

import com.thatblackbwoy.couponservice.dto.CustomerDto;
import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.exception.BusinessException;
import com.thatblackbwoy.couponservice.model.BlockSecondUser;
import com.thatblackbwoy.couponservice.model.Coupon;
import com.thatblackbwoy.couponservice.model.CouponUsage;
import com.thatblackbwoy.couponservice.model.Customer;
import com.thatblackbwoy.couponservice.repository.BlockSecondUserRepository;
import com.thatblackbwoy.couponservice.repository.CouponRepository;
import com.thatblackbwoy.couponservice.repository.CouponUsageRepository;
import com.thatblackbwoy.couponservice.repository.CustomerRepository;
import com.thatblackbwoy.couponservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final BlockSecondUserRepository blockSecondUserRepository;
    private static final ReentrantLock lock = new ReentrantLock();
    @Override
    public ApiResponse addCustomer(CustomerDto customerDto) {
        Customer customer = Customer.builder()
                .name(customerDto.getName())
                .lastRequestTime(LocalDateTime.now())
                .build();
        Customer response = customerRepository.save(customer);
        return ApiResponse.builder()
                .message("Customer added successfully")
                .data(response)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse useCoupon(Long customerId, String couponCode) throws BusinessException {
//        lock.tryLock();
        Coupon coupon = couponRepository.findByCouponCode(couponCode);
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new BusinessException("Id not found"));
        LocalDateTime threeMinutesAgo = LocalDateTime.now().minusMinutes(3);
        CouponUsage notUsedThreeMinutesAgo = couponUsageRepository.findByCustomerIdAndDateUsedAfter(customerId, threeMinutesAgo); //userTable
        CouponUsage couponUsage = CouponUsage.builder().customer(customer).coupon(coupon).dateUsed(LocalDateTime.now()).build();
        if (coupon != null && !coupon.isUsed() && LocalDateTime.now().isAfter(coupon.getExpirationDate())) {
            return ApiResponse.builder()
                    .message("Coupon has expired")
                    .data(coupon.getStatus())
                    .build();
        }
        if (coupon != null && coupon.isUsed()) {
            return ApiResponse.builder()
                    .message("Coupon has been used")
                    .data(coupon.getStatus())
                    .build();
        }
        if (coupon != null && LocalDateTime.now().isBefore(coupon.getExpirationDate()) && notUsedThreeMinutesAgo == null) {
            if (lock.tryLock()) {
                try {
                    coupon.setUsed(true);
                    coupon.setStatus("101");
                    couponRepository.save(coupon);
                    couponUsage.setCustomer(customer);
                    couponUsage.setDateUsed(LocalDateTime.now());
                    couponUsageRepository.save(couponUsage);
                    return ApiResponse.builder()
                            .message("Coupon used successfully")
                            .data(coupon.getStatus())
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            throw new BusinessException("currently in use");
        }
        throw new BusinessException("You have already used a coupon today, wait till after 24 hrs before you can use another one");
    }
}
