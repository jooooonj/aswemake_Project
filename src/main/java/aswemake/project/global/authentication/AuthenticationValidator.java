package aswemake.project.global.authentication;

import aswemake.project.domain.member.exception.CustomAccessDeniedException;
import aswemake.project.global.config.AppConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationValidator {
    public void checkMart(String username){
        if(!username.equals(AppConfig.getAdminEmail()))
            throw new CustomAccessDeniedException("마트 관리자 외에는 접근 할 수 없습니다.");
    }

    public void checkMember(String username){
        if(username.equals(AppConfig.getAdminEmail()))
            throw new CustomAccessDeniedException("일반 회원 외에는 접근 할 수 없습니다.");
    }
}
