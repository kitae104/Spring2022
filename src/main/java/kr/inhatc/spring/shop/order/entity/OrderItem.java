package kr.inhatc.spring.shop.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 주문
 * 
 * @author 김기태
 *
 */
@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity
{

	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;

	// 하나의 상품은 여러 주문 상품으로 들어갈 수 있음(상품 기준으로 다대일 단방향 매핑)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id") // FK
	private Item item;

	// 한 번의 주문에 여러 개의 상품을 주문(주문 상품 엔티티와 주문 엔티티를 다대일 단방향으로 매핑)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id") // FK
	private Order order;

	private int orderPrice; // 주문가격

	private int count; // 수량

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;

	/**
	 * 주문할 상품과 수량을 통해 주문 객체 생성
	 * @param item 상품
	 * @param count 수량
	 * @return
	 */
	public static OrderItem createOrderItem(Item item, int count)
	{
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);					// 상품 설정
		orderItem.setCount(count);					// 수량 설정 
		orderItem.setOrderPrice(item.getPrice());	// 가격 설정
		
		item.removeStock(count);					// 주문 수량만큼 재고 수량을 감소
		return orderItem;
	}
	
	/**
	 * 주문 가격과 주문 수량을 곱해서 해당 상품을 주문한  총 가격을 계산하는 메소드 
	 * @return
	 */
	public int getTotalPrice()
	{
		return orderPrice * count;
	}
	
	/**
	 * 주문을 취소할 경우 주문 수량 만큼 재고를 증가시키는 기능
	 */
	public void cancel()
	{
		this.getItem().addStock(count);
	}
}
