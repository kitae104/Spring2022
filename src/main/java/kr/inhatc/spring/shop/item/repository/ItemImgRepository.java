package kr.inhatc.spring.shop.item.repository;

import kr.inhatc.spring.shop.item.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 이미지 정보 저장을 위한 레포지토리
 */
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
}
