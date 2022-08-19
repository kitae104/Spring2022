package kr.inhatc.spring.shop.item.service;

import kr.inhatc.spring.shop.item.dto.ItemFormDto;
import kr.inhatc.spring.shop.item.dto.ItemImgDto;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.entity.ItemImg;
import kr.inhatc.spring.shop.item.repository.ItemImgRepository;
import kr.inhatc.spring.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품을 등록하는 역할 수행
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    /**
     * 아이템 저장하기 <br/>
     * 상품등록, 이미지 등록
     * @param itemFormDto 폼에서 받은 item 정보
     * @param itemImgFileList 파일 리스트
     * @return 생성된 아이템 번호
     * @throws Exception
     */
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = itemFormDto.createItem();   // 상품 등록 폼으로부터 입력 받은 데이터를 이용해서 item 개체를 생성
        itemRepository.save(item);

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg  itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0){
                itemImg.setRepimgYn("Y");   // 첫번째 이미지일 경우 대표 상품 이미지로 설정
            } else {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));    // 상품의 이미지 정보를 저장
        }
        return item.getId();
    }

    /**
     * 상품 상세 가져오기
     * @param itemId 아이템 번호
     * @return ItemFormDto
     */
    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
    	
    	// 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);							// 상품 엔티티 업데이트 
        
        // 상품 아이디 리스트 조회
        List<Long> itemImgIds = itemFormDto.getItemImgIds();	
        
        // 이미지 등록 
        for (int i = 0; i < itemImgFileList.size(); i++)
		{
        	// 상품 이미지를 업데이트 하기 위해서 
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}
        
        return item.getId();
    }

}
