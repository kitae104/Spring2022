package kr.inhatc.spring.shop.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.shop.cart.dto.CartDetailDto;
import kr.inhatc.spring.shop.cart.entity.CartItem;
import org.springframework.data.jpa.repository.Query;

/**
 * 장바구니에 들어갈 상품을 저장하거나 조회하기 위해서 사용 
 * @author 김기태
 *
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long>
{
	CartItem findByCartIdAndItemId(Long cartId, Long itemId);
	
	// CartDetailDto의 생성자를 이용하여 DTO를 변환할 때, new 키워드와 해당 DTO의 패키지, 클래스명을 적어준다. 
	// 또한 생성자의 파라미터 순서는 DTO 클래스에 명시한 순으로 넣어준다. 
	@Query("select new kr.inhatc.spring.shop.cart.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
			"from CartItem ci, ItemImg im " +
			"join ci.item i " + 
			"where ci.cart.id= :cartId " + 
			"and im.item.id = ci.item.id " + 
			"and im.repimgYn = 'Y' " +
			"order by ci.regTime desc")
	List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
