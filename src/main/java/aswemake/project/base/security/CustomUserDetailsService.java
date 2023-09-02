package aswemake.project.base.security;

import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email+"로 가입된 계정이 없습니다."));
        return new CustomUser(member.getEmail(), member.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("DEFAULT")));
    }
}
