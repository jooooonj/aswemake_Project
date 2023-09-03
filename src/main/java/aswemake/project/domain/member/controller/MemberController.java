package aswemake.project.domain.member.controller;

import aswemake.project.domain.member.entity.request.JoinMemberRequestDto;
import aswemake.project.domain.member.entity.request.LoginMemberRequestDto;
import aswemake.project.domain.member.service.MemberService;
import aswemake.project.global.jwt.JwtToken;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/join")
    public ResponseEntity<Long> join(@Valid @RequestBody JoinMemberRequestDto joinMemberRequestDto) {
        Long memberId = memberService.join(joinMemberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Valid @RequestBody LoginMemberRequestDto loginMemberRequestDto, HttpServletResponse response) {
        JwtToken jwtToken = memberService.login(loginMemberRequestDto);
        response.addHeader("Authentication", jwtToken.getAccessToken());

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }

}
