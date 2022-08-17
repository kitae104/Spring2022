package kr.inhatc.spring.shop.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    @Value(value = "${itemImgLocation}")    // 실제 상품 이미지를 업로드할 경로 설정
    private String itemImgLocation;
}
