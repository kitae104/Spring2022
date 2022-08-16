package kr.inhatc.spring.shop.item.dto;

import kr.inhatc.spring.shop.constant.ItemSellStatus;
import kr.inhatc.spring.shop.item.entity.Item;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ItemFormDto {

    private Long id;       //상품 코드

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm; //상품명

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int price; //가격

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private int stockNumber; //재고수량

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private String itemDetail; //상품 상세 설명

    private ItemSellStatus itemSellStatus; //상품 판매 상태

    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();    // 아이템 이미지 리스트

    // 상품 등록 시에는 아직 상품의 이미지를 저장하지 않았기 때문에 아무값도 들어가 있지 않고 수정 시에 이미지 아이디를 담아올 용도로 사용
    private List<Long> itemImgIds = new ArrayList<>();              // 이미지 아이디 리스트

    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * 엔티티 객체와 Dto 객체 간의 데이터를 복사
     * @return
     */
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    /**
     * 엔티티 객체와 Dto 객체 간의 데이터를 복사
     * @return
     */
    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }
}
