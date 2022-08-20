package kr.inhatc.spring.shop.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.inhatc.spring.shop.item.dto.ItemSearchDto;
import kr.inhatc.spring.shop.item.entity.Item;

public interface ItemRepositoryCustom
{	
	/**
	 * 상품 조회 페이지
	 * @param itemSearchDto 상품 조회 조건
	 * @param pageable 페이지 정보
	 * @return
	 */
	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
