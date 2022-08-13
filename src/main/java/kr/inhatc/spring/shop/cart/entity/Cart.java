package kr.inhatc.spring.shop.cart.entity;

import kr.inhatc.spring.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")    // 외래키 설정 - 자동도 가능하지만 명확하게 설정
    private Member member;
}
