package kr.inhatc.spring.shop.item.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.inhatc.spring.shop.constant.ItemSellStatus;
import kr.inhatc.spring.shop.item.dto.ItemFormDto;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.entity.ItemImg;
import kr.inhatc.spring.shop.item.repository.ItemImgRepository;
import kr.inhatc.spring.shop.item.repository.ItemRepository;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    // 가짜 MultipartFile 리스트를 만들어 반환해주는 메소드
    List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String path = "E:/Temp/shop/item";
            String imgName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imgName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveItemTest() throws Exception {
        // ItemFormDto 생성
        ItemFormDto itemFormDto = new ItemFormDto();            // 상품 데이터 세팅
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();

        // 상품 데이터와 이미지 정보를 파라미터로 넘겨 저장한 후 저장된 상품의 아이디 값을 반환
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm());    // 입력한 상품 데이터와 실제 저장된 상품 데이터가 같은지 확인
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName()); // 첫번째 파일의 이름이 같은지 확인
    }
}