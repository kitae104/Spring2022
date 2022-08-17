package kr.inhatc.spring.shop.order.entity;

import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    // 하나의 상품은 여러 주문 상품으로 들어갈 수 있음(상품 기준으로 다대일 단방향 매핑)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")       // FK
    private Item item;

    // 한 번의 주문에 여러 개의 상품을 주문(주문 상품 엔티티와 주문 엔티티를 다대일 단방향으로 매핑)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")      // FK
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;
}
