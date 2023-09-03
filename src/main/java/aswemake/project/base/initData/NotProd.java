package aswemake.project.base.initData;
import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.entity.MemberRoleType;
import aswemake.project.domain.member.repository.MemberRepository;
import aswemake.project.global.config.AppConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"dev", "test"})
@Transactional
public class NotProd {
    @Bean
    CommandLineRunner initData(
            PasswordEncoder passwordEncoder,
            MemberRepository memberRepository
    ) {
        return args -> {
            //마트관리자
            memberRepository.save(
                    Member.builder()
                            .email(AppConfig.getAdminEmail())
                            .password(passwordEncoder.encode(AppConfig.getAdminPassword()))
                            .roleType(MemberRoleType.MART_ADMIN)
                            .build());

            //일반회원
            memberRepository.save(
                    Member.builder()
                            .email("member1@naver.com")
                            .password(passwordEncoder.encode("12345678"))
                            .roleType(MemberRoleType.MEMBER)
                            .build());
        };
    }
}
