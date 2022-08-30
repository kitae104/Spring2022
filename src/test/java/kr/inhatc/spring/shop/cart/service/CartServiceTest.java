package kr.inhatc.spring.shop.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import kr.inhatc.spring.shop.cart.dto.CartItemDto;
import kr.inhatc.spring.shop.cart.entity.CartItem;
import kr.inhatc.spring.shop.cart.repository.CartItemRepository;
import kr.inhatc.spring.shop.constant.ItemSellStatus;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.repository.ItemRepository;

@SpringBootTest
@Transactional
class CartServiceTest
{
	@Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;
	
    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test2@test.com");
        return memberRepository.save(member);
    }

    @Test
	@DisplayName("장바구니 담기 테스트")
	public void addCartTest() throws Exception
	{
		Item item = saveItem();
		Member member =  saveMember();
		
		CartItemDto cartItemDto = new CartItemDto();
		cartItemDto.setCount(5);
		cartItemDto.setItemId(item.getId());
		
		Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
		
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		
		assertEquals(item.getId(), cartItem.getItem().getId());
		assertEquals(cartItemDto.getCount(), cartItem.getCount());	
		
	}
}
