package kr.inhatc.spring.shop.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.shop.item.entity.ItemImg;

/**
 * 상품 이미지 정보 저장을 위한 레포지토리
 */
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    /**
     * 상품의 대표 이미지를 찾는 메소드
     * 구매 이력 페이지에서 주문 상품의 대표 이미지를 보여주기 위해 추가 
     * @param itemId
     * @param repimgYn
     * @return
     */
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}
