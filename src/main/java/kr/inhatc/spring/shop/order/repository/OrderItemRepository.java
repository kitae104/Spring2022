package kr.inhatc.spring.shop.order.repository;

import kr.inhatc.spring.shop.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
