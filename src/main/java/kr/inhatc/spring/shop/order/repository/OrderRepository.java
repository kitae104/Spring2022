package kr.inhatc.spring.shop.order.repository;

import kr.inhatc.spring.shop.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
