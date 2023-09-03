package aswemake.project.global.jwt;

import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.entity.MemberRoleType;
import aswemake.project.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 Authorization 값을 가져온다.
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length());

            if (jwtTokenProvider.verify(token)) { //token이 유효하면
                Map<String, Object> claims = jwtTokenProvider.getClaimsToMap(token);
                String email = (String) claims.get("email");

                Member member = memberService.findByEmail(email);
                forceAuthentication(member);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 강제로 로그인 처리하는 메소드
    private void forceAuthentication(Member member) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (member.getRoleType().getValue().equals(MemberRoleType.MART_ADMIN.getValue())){
            log.info("로그인 : {}", "마트 관리자입니다.");
            authorities.add(new SimpleGrantedAuthority(MemberRoleType.MART_ADMIN.getValue()));
        }

        authorities.add(new SimpleGrantedAuthority(MemberRoleType.MEMBER.getValue()));
        User user = new User(member.getEmail(), member.getPassword(), authorities);

        // 스프링 시큐리티 객체에 저장할 authentication 객체를 생성
        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        authorities
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
