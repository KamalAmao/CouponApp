package com.thatblackbwoy.couponservice.repository;

import com.thatblackbwoy.couponservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
//    Customer findByLastRequestTime(Long customerId);
}

