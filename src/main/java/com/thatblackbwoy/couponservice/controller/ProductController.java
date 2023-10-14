package com.thatblackbwoy.couponservice.controller;

import com.thatblackbwoy.couponservice.dto.ProductDto;
import com.thatblackbwoy.couponservice.dto.response.ApiResponse;
import com.thatblackbwoy.couponservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto){
        try{
            return ResponseEntity.ok().body(productService.addProduct(productDto));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().build());
        }
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> buyWithCoupon(@PathVariable Long productId, @RequestParam String couponCode){
        try{
            return ResponseEntity.ok().body(productService.buyWithCoupon(productId, couponCode));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().build());
        }
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductOriginalPrice(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok().body(productService.buyAtProductOriginalPrice(productId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().build());
        }
    }
}
