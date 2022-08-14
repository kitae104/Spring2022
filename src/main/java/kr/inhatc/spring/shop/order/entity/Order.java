package kr.inhatc.spring.shop.order.entity;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.shop.constant.OrderStatus;
import kr.inhatc.spring.utils.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // SQL 키워드 때문에 이름 변경
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 한 명의 회원은 여러번 주문을 할 수 있기 때문에 다대일 단방향 매핑 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    // 열거형은 문자열로 취급해야 오류를 방지 할 수 있음
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    // 양방향 설정
    // 연관 관계의 주인은 OrderItem에 존재
    // 주인이 아니기 때문에 mappedBy 속성으로 연관 관계의 주인인 "order"를 설정
    // "order"라 적는 이유는 OrderItem에 있는 order에 의해 관리된다는 의미임.
    // 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeType.ALL 설정
    // 고아 객체 제거를 사용하기 위해 orphanRemoval = true 사용
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); // 하나의 주문이 여러 개의 주문 상품을 갖기 때문에 리스트로

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;
}
