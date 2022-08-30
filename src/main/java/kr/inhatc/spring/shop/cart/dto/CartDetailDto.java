package kr.inhatc.spring.shop.cart.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 장바구니 조회에 사용할 DTO
 * @author 김기태
 *
 */
@Getter
@Setter
public class CartDetailDto
{
	private Long cartItemId; 	//장바구니 상품 아이디

    private String itemNm; 		//상품명

    private int price; 			//상품 금액

    private int count; 			//수량

    private String imgUrl; 		//상품 이미지 경로

    /**
     * 장바구니 페이지에 전달할 데이터를 생성자로 만듬.
     * @param cartItemId
     * @param itemNm
     * @param price
     * @param count
     * @param imgUrl
     */
	public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl)
	{
		super();
		this.cartItemId = cartItemId;
		this.itemNm = itemNm;
		this.price = price;
		this.count = count;
		this.imgUrl = imgUrl;
	}
    
}
