package kr.inhatc.spring.shop.cart.repository;

import kr.inhatc.spring.shop.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
