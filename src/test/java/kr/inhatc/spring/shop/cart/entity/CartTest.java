package kr.inhatc.spring.shop.cart.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.inhatc.spring.member.dto.MemberFormDto;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import kr.inhatc.spring.shop.cart.repository.CartRepository;

@SpringBootTest
@Transactional
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){   // MemberServiceTest 참조
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();     // 데이터베이스에 반영을 위해 사용
        em.clear();     // 영속성 컨텍스트 비우기

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(savedCart.getMember().getId(), member.getId());

        // 기존 방식
//        Optional<Cart> savedCart = cartRepository.findById(cart.getId());

//        if(savedCart.isPresent()){
//            assertEquals(savedCart.get().getMember().getId(), member.getId());
//        } else {
//            throw new EntityNotFoundException();
//        }

    }
}