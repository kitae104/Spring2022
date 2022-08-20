package kr.inhatc.spring.shop.item.dto;

import kr.inhatc.spring.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 데이터 조회 시 상품 조회 조건을 처리
 */
@Setter
@Getter
public class ItemSearchDto
{
	private String searchDateType;              // 조회 시간 타입(모두 / 하루 / 일주일 / 1개월 / 6개월)

    private ItemSellStatus searchSellStatus;    // 판매 상태(판매 / 완료)

    private String searchBy;                    // 조회 유형(상품명 / 상품 등록자 아이디)

    private String searchQuery = "";			// 조회할 검색어
}
