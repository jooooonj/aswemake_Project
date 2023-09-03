package aswemake.project.domain.member.service;

import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.entity.request.JoinMemberRequest;
import aswemake.project.domain.member.entity.request.LoginMemberRequest;
import aswemake.project.domain.member.exception.MemberNotFoundException;
import aswemake.project.domain.member.exception.MemberPasswordNotCorrectException;
import aswemake.project.domain.member.repository.MemberRepository;
import aswemake.project.global.jwt.JwtToken;
import aswemake.project.global.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new MemberNotFoundException(email+"로 가입된 계정이 존재하지 않습니다.")
        );
    }

    @Transactional
    public Long join(@Valid JoinMemberRequest joinMemberRequest){
        Member member = Member.builder()
                .email(joinMemberRequest.getEmail())
                .password(passwordEncoder.encode(joinMemberRequest.getPassword()))
                .build();

        return memberRepository.save(member).getId();
    }

    public JwtToken login(@Valid LoginMemberRequest loginMemberRequest) {
        Member member = findByEmail(loginMemberRequest.getEmail());

        if (!passwordEncoder.matches(loginMemberRequest.getPassword(), member.getPassword()))
            throw new MemberPasswordNotCorrectException("비밀번호가 일치하지 않습니다.");

        return jwtTokenProvider.genToken(member.toClaims(), member.getRoleType().getValue(), 60 * 60 * 1); //1시간 (임의설정)
    }
}
