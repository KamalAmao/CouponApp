package com.thatblackbwoy.couponservice.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double originalPrice;
    private double discountPrice;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "coupon_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Coupon coupon;
}
