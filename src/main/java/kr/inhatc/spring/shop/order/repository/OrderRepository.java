package kr.inhatc.spring.shop.order.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.inhatc.spring.shop.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * 현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회
	 * @param email
	 * @param pageable
	 * @return
	 */
	@Query("select o from Order o where o.member.email = :email order by o.orderDate desc")
	List<Order> findOrders(@Param("email") String email, Pageable pageable);

	/**
	 * 현재 로그인한 회원의 주문 개수가 몇개 인지 조회
	 * @param email
	 * @return
	 */
	@Query("select count(o) from Order o where o.member.email = :email")	
	Long countOrder(@Param("email") String email);
}
