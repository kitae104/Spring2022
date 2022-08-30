package kr.inhatc.spring.shop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.shop.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> 
{
	/**
	 * 로그인한 회원의 Cart 엔티티를 찾기 위해서 CartRepository에 쿼리 메소드를 추가
	 * @param memberId
	 * @return
	 */
	Cart findByMemberId(Long memberId);
}
