package kr.inhatc.spring.shop.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import kr.inhatc.spring.shop.constant.ItemSellStatus;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.repository.ItemRepository;
import kr.inhatc.spring.shop.order.dto.OrderDto;
import kr.inhatc.spring.shop.order.entity.Order;
import kr.inhatc.spring.shop.order.entity.OrderItem;
import kr.inhatc.spring.shop.order.repository.OrderRepository;

@SpringBootTest
@Transactional
class OrderServiceTest
{
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	MemberRepository memberRepository;

	/**
	 * 상품 정보 저장 
	 * 
	 * @return
	 */
	public Item saveItem()
	{
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		return itemRepository.save(item);
	}

	/**
	 * 회원 정보 저장 
	 * 
	 * @return
	 */
	public Member saveMember()
	{
		Member member = new Member();
		member.setEmail("test1@test.com");
		return memberRepository.save(member);

	}

	@Test
	@DisplayName("주문 테스트")
	public void orderTest() throws Exception
	{
		Item item = saveItem();
		Member member = saveMember();

		OrderDto orderDto = new OrderDto();
		orderDto.setCount(10);					// 상품 수량
		orderDto.setItemId(item.getId());		// 주문할 상품 

		// 주문 로직 호출 결과 생성된 주문 번호를 orderId 변수에 저장 
		Long orderId = orderService.order(orderDto, member.getEmail());
		
		// 주문 번호를 이용해서 저장된 주문 정보를 조회 
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		
		
		List<OrderItem> orderItems = order.getOrderItems();

		// 주문한 상품의 총 가격 구하기 
		int totalPrice = orderDto.getCount() * item.getPrice();		
		
		assertEquals(totalPrice, order.getTotalPrice());
	}

//	@Test
//    @DisplayName("주문 취소 테스트")
//    public void cancelOrderTest(){
//        Item item = saveItem();
//        Member member = saveMember();
//
//        OrderDto orderDto = new OrderDto();
//        orderDto.setCount(10);
//        orderDto.setItemId(item.getId());
//        Long orderId = orderService.order(orderDto, member.getEmail());
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(EntityNotFoundException::new);
//        orderService.cancelOrder(orderId);
//
//        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
//        assertEquals(100, item.getStockNumber());
//    }
	
}
