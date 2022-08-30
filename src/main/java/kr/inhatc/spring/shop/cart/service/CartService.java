package kr.inhatc.spring.shop.cart.service;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.EncryptedPrivateKeyInfo;

import javax.crypto.interfaces.DHPrivateKey;
import javax.persistence.EntityNotFoundException;

import kr.inhatc.spring.shop.cart.dto.CartDetailDto;
import kr.inhatc.spring.shop.cart.dto.CartItemDto;
import kr.inhatc.spring.shop.cart.entity.Cart;
import kr.inhatc.spring.shop.cart.entity.CartItem;
import kr.inhatc.spring.shop.cart.repository.CartItemRepository;
import kr.inhatc.spring.shop.cart.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        // 장바구니에 담을 상품 엔티티를 조회
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 현재 로그인 한 회원 엔티티를 조회
        Member member = memberRepository.findByEmail(email);

        // 현재 로그인 한 회원의 장바구니 엔티티 조회
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 상품을 처음에 장바구니에 담을 경우 해당 회원의 장바구니 엔티티를 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 현재 상품이 장바구니에 이미 들어가 있는지를 조회
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수향 만큼을 더해준다.
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            // 장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량을 이용하여 CartItem 엔티티를 생성
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);        // 장바구니에 들어갈 상품을 저장
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);

        // 현재 로그인한 회원의 장바구니 엔티티를 조회
        Cart cart = cartRepository.findByMemberId(member.getId());

        //장바구니에 상품을 한 번도 안 담았을 경우 장바구니 엔티티가 없으므로 빈 리스트를 반환
        if (cart == null) {
            return cartDetailDtoList;
        }

        // 장바구니에 담겨 있는 상품 정보를 조회
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        // 현재 로그인한 회원을 조회
		Member curMember = memberRepository.findByEmail(email);
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        // 장바구니 상품을 저장한 회원을 조회
        Member savedMember = cartItem.getCart().getMember();

        // 로그인한 회원가 장바구니 상품을 저장한 회원이 다를 경우
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
			return false;
		}
		return true;
    }

    /**
     * 장바구니 상품의 수향을 업데이트하는 기능 
     * @param cartItemId
     * @param count
     */
    public void updateCartItemCount(Long cartItemId, int count)
    {
    	CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
    	cartItem.updateCount(count);
    }
}
