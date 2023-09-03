package aswemake.project.domain.member.validate;

import aswemake.project.domain.member.exception.NotAdminAccessDeniedException;
import aswemake.project.global.config.AppConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdmValidator {
    public void checkAdmin(String username){
        if(!username.equals(AppConfig.getAdminEmail()))
            throw new NotAdminAccessDeniedException("마트 관리자 외에는 접근 할 수 없습니다.");
    }
}
