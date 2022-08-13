package kr.inhatc.spring.shop.cart.entity;

import kr.inhatc.spring.shop.item.entity.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 장바구니 아이템
 */
@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    // 하나의 바구니에는 여러개의 상품을 담을 수 있음
    @ManyToOne
    @JoinColumn(name = "cart_id")   // FK로 사용
    private Cart cart;

    // 하나의 상품은 여러 장바구니에 담길 수 있음
    @ManyToOne
    @JoinColumn(name = "item_id")   // FK로 사용
    private Item item;

    private int count;              // 같은 상품을 장바구니에 몇개 담을지

    /**
     * 장바구니에 담을 상품 엔티티를 생성하는 메소드
     *
     * @param cart
     * @param item
     * @param count
     * @return
     */
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();		// 카트 아이템 생성
        cartItem.setCart(cart); 				// 카트 아이템에 카드 설정
        cartItem.setItem(item);					// 카트 아이템에 아이템 설정
        cartItem.setCount(count); 				// 카트 아이템에 수량 설정
        return cartItem;
    }

    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
