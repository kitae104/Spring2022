package kr.inhatc.spring.shop.item.dto;

import org.modelmapper.ModelMapper;

import kr.inhatc.spring.shop.item.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 저장 후 상품 이미지에 대한 데이터를 전달한 DTO 클래스
 */
@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * 엔티티 객체를 파라미터로 받아서 itemImg 객체의 자료형과 멤버 변수의 이름이 같을 때 ItemImgDto로 값을 복사해 반환
     * @param itemImg
     * @return
     */
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
    }
}
