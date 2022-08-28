package kr.inhatc.spring.shop.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.shop.constant.OrderStatus;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders") // SQL 키워드 때문에 이름 변경
@Getter
@Setter
public class Order extends BaseEntity
{

	@Id
	@GeneratedValue
	@Column(name = "order_id")
	private Long id;

	// 한 명의 회원은 여러번 주문을 할 수 있기 때문에 다대일 단방향 매핑 설정
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private LocalDateTime orderDate; // 주문일

	// 열거형은 문자열로 취급해야 오류를 방지 할 수 있음
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // 주문상태

	// 양방향 설정
	// 연관 관계의 주인은 OrderItem에 존재
	// 주인이 아니기 때문에 mappedBy 속성으로 연관 관계의 주인인 "order"를 설정
	// "order"라 적는 이유는 OrderItem에 있는 order에 의해 관리된다는 의미임.
	// 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeType.ALL 설정
	// 고아 객체 제거를 사용하기 위해 orphanRemoval = true 사용
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems = new ArrayList<>(); // 하나의 주문이 여러 개의 주문 상품을 갖기 때문에 리스트로

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;

	/**
	 * orderItems에는 주문 상품 정보를 담아준다.
	 * 
	 * @param orderItem
	 */
	public void addOrderItem(OrderItem orderItem)
	{
		orderItems.add(orderItem);
		orderItem.setOrder(this); // Order 엔티티와 OrderItem 엔티티가 양방향 참조관계이므로,
	}

	/**
	 * 주문 객체 만들기
	 * 
	 * @param member
	 * @param orderItemList
	 * @return
	 */
	public static Order createOrder(Member member, List<OrderItem> orderItemList)
	{
		Order order = new Order();
		order.setMember(member); // 상품을 주문한 회원정보 설정

		for (OrderItem orderItem : orderItemList) // 한 번에 여러개의 상품 주문을 처리
		{
			order.addOrderItem(orderItem);
		}

		order.setOrderStatus(OrderStatus.ORDER); // 주문 상태 설정
		order.setOrderDate(LocalDateTime.now()); // 주문 시간 설정

		return order;
	}

	/**
	 * 전체 금액을 계산해서 반환하는 기능
	 * @return
	 */
	public int getTotalPrice()
	{
		int totalPrice = 0; 
		
		for (OrderItem orderItem : orderItems)
		{
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
	}
	
	/**
	 * 주문 취소 시 주문 수량을 재고에 더해주는 로직과 주문 상태를 취소 상태로 
	 * 바꿔주는 기능 
	 */
	public void cancelOrder() {
		this.orderStatus = OrderStatus.CANCEL;
		
		for (OrderItem orderItem : orderItems)
		{
			orderItem.cancel();
		}
	}
}
