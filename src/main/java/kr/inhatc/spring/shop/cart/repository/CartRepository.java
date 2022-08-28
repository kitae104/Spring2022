package kr.inhatc.spring.shop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.shop.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
