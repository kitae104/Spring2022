package kr.inhatc.spring.shop.item.service;

import kr.inhatc.spring.shop.item.entity.ItemImg;
import kr.inhatc.spring.shop.item.repository.ItemImgRepository;
import kr.inhatc.spring.utils.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    @Value(value = "${itemImgLocation}")    // 실제 상품 이미지를 업로드할 경로 설정
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    /**
     * 상품 이미지를 저장하는 메소드
     * @param itemImg
     * @param itemImgFile
     * @throws Exception
     */
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";    // 로컬에 저장된 파일 이름
        String imgUrl = "";     // 이미지 파일 경로

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){

            // 파일을 업로드하고 해당 파일 이름을 반환
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // 저장한 상품 이미지를 불러올 경로를 설정 --> WebMvcConfig에 리소스 위치 설정
            imgUrl = "/images/item/"+imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
}
