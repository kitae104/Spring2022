package kr.inhatc.spring.shop.item.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import kr.inhatc.spring.exceptions.shop.OutOfStockException;
import kr.inhatc.spring.shop.constant.ItemSellStatus;
import kr.inhatc.spring.shop.item.dto.ItemFormDto;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity
{
	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 상품 코드

	@Column(nullable = false, length = 50)
	private String itemNm; // 상품명

	@Column(name = "price", nullable = false)
	private int price; // 가격

	@Column(nullable = false)
	private int stockNumber; // 재고수량

	@Lob
	@Column(nullable = false)
	private String itemDetail; // 상품 상세 설명

	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus; // 상품 판매 상태

	// extends BaseEntity 추가 후 삭제
//    private LocalDateTime regTime;  // 등록 시간
//
//    private LocalDateTime updateTime;   // 수정 시간

	/**
	 * 상품 업데이트
	 * 
	 * @param itemFormDto
	 */
	public void updateItem(ItemFormDto itemFormDto)
	{
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}

	/**
	 * 상품을 주문할 경우 재고를 감소 시킴
	 * @param stockNumber
	 */
	public void removeStock(int stockNumber)
	{
		int restStock = this.stockNumber - stockNumber;
		if(restStock < 0)			
		{
			throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
		}
		this.stockNumber = restStock;	// 남은 재고를 현재의 재고로 수정 
	}
	
	
}
