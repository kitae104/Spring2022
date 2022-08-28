package kr.inhatc.spring.shop.order.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.entity.ItemImg;
import kr.inhatc.spring.shop.item.repository.ItemImgRepository;
import kr.inhatc.spring.shop.item.repository.ItemRepository;
import kr.inhatc.spring.shop.order.dto.OrderDto;
import kr.inhatc.spring.shop.order.dto.OrderHistDto;
import kr.inhatc.spring.shop.order.dto.OrderItemDto;
import kr.inhatc.spring.shop.order.entity.Order;
import kr.inhatc.spring.shop.order.entity.OrderItem;
import kr.inhatc.spring.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

/**
 * 주문과 관련된 서비스 로직 
 * @author 김기태
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService
{
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	private final ItemImgRepository itemImgRepository;
	
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
	
	@Transactional(readOnly = true)
	public Page<OrderHistDto> getOrderList(String email, Pageable pageable)
	{
		// 유저의 아이디와 페이징 조건을 이용해서 주문 목록을 조회 
		List<Order> orders = orderRepository.findOrders(email, pageable);
		
		// 유저의 주문 총 개수를 구함 
		Long totalCount = orderRepository.countOrder(email);
		
		List<OrderHistDto> orderHistDtos = new ArrayList<>();
		
		// 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO를 생성 
		for (Order order : orders)
		{
			OrderHistDto orderHistDto = new OrderHistDto(order);
			List<OrderItem> orderItems = order.getOrderItems();
			
			for (OrderItem orderItem : orderItems)
			{
				// 주문 상품의 대표 이미지 조회 
				ItemImg itemImg = itemImgRepository
						.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
				OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
				orderHistDto.addOrderItemDto(orderItemDto); 
			}
			orderHistDtos.add(orderHistDto);
		}
		
		// 페이지 구현 객체를 생성하여 반환 
		return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
	}
	
	/**
	 * 주문을 취소하는 기능<br>
	 * 
	 * @param orderId
	 * @param email
	 * @return
	 */
	@Transactional(readOnly = true)	
	public boolean validateOrder(Long orderId, String email) 
	{
		Member curMember = memberRepository.findByEmail(email);
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = order.getMember();
		
		// 현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사 
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail()))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 주문 취소 상태로 변경하면 변경 감지 기능에 의해서 트랜잭션이 끝날 때 update 쿼리 실행 
	 * @param orderId
	 */
	public void cancelOrder(Long orderId)
	{
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		order.cancelOrder();
	}
}
