package kr.inhatc.spring.shop.cart.entity;

import java.security.spec.EdECPublicKeySpec;

import javax.crypto.interfaces.DHPublicKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.slf4j.impl.StaticMarkerBinder;

import ch.qos.logback.core.joran.action.NewRuleAction;
import groovyjarjarantlr4.v4.parse.GrammarTreeVisitor.lexerBlock_return;
import kr.inhatc.spring.member.controller.MemberController;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")    // 외래키 설정 - 자동도 가능하지만 명확하게 설정
    private Member member;
    
    /**
     * 처음 장바구니에 상품을 담을 때 해당 회원의 장바구니를 생성
     * 회원 엔티티를 파라미터로 받아서 장바구니 ㅇ네티티를 생성 
     * @param member
     * @return
     */
    public static Cart createCart(Member member)
    {
    	Cart cart = new Cart();
    	cart.setMember(member);
    	return cart;
    }
}
