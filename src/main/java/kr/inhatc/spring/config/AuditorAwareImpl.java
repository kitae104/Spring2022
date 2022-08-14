package kr.inhatc.spring.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 현재 로그인한 사용자의 정보를 등록자와 수정자로 지정하기 위해서
 * AuditorAware 인터페이스를 구현한 클래스 생성
 */
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();  // 로그인 된 사용자의 이름을 등록자와 수정자로 지정
        }
        return Optional.of(userId);
    }
}
