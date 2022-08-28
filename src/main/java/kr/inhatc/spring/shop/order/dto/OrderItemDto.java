package kr.inhatc.spring.shop.order.dto;

import kr.inhatc.spring.shop.order.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 데이터를 화면에 보낼 때 사용할 DTO 
 * @author 김기태
 *
 */
@Getter
@Setter
public class OrderItemDto
{
	private String itemNm;	// 상품명 
	
	private int count; 		//주문 수량

    private int orderPrice; //주문 금액
    
    private String imgUrl; 	//상품 이미지 경로

    /**
     * 생성자로 orderItem 정보와 이미지 경로 설정  
     * @param orderItem
     * @param imgUrl
     */
	public OrderItemDto(OrderItem orderItem, String imgUrl)
	{
		super();
		this.itemNm = orderItem.getItem().getItemNm();
		this.count = orderItem.getCount();
		this.orderPrice = orderItem.getOrderPrice();
		this.imgUrl = imgUrl;
	}
    
    
}
