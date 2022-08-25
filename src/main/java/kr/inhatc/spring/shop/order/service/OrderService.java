package kr.inhatc.spring.shop.order.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.repository.ItemRepository;
import kr.inhatc.spring.shop.order.dto.OrderDto;
import kr.inhatc.spring.shop.order.entity.Order;
import kr.inhatc.spring.shop.order.entity.OrderItem;
import kr.inhatc.spring.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService
{
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	
	public Long order(OrderDto orderDto, String email)
	{
		// 주문할 상품을 조회
		Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
		
		// 이메일 정보를 이용해서 회원 정보 조회 
		Member member = memberRepository.findByEmail(email);
		
		List<OrderItem> orderItemList = new ArrayList<>();
		
		// 주문할 상품 엔티티와 주문 주량을 이용해서 주문 상품 엔티티를 생성 
		OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
		orderItemList.add(orderItem);
		
		// 회원 정보와 상품 리스트 정보를 이용하여 주문 엔티티를 생성
		Order order = Order.createOrder(member, orderItemList);
		
		// 생성한 주문 엔티티 저장
		orderRepository.save(order);	
		
		return order.getId();
	}
	
	
}
