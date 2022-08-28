package kr.inhatc.spring.shop.order.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kr.inhatc.spring.shop.constant.OrderStatus;
import kr.inhatc.spring.shop.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 정보를 담기 위한 DTO
 * @author 김기태
 *
 */
@Getter
@Setter
public class OrderHistDto
{
	private Long orderId; 				//주문아이디
	
    private String orderDate; 			//주문날짜
    
    private OrderStatus orderStatus; 	//주문 상태
	
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();	// 주문 상품 리스트
    	
	public OrderHistDto(Order order)
	{
		this.orderId = order.getId();
		// 날짜 포맷 수정 
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		this.orderStatus = order.getOrderStatus();
	}
	
	/**
	 * orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드 
	 * @param orderItemDto
	 */
	public void addOrderItemDto(OrderItemDto orderItemDto)
	{
		orderItemDtoList.add(orderItemDto);
	}
}
