package aswemake.project.domain.member.controller;
import aswemake.project.domain.member.entity.request.JoinMemberRequestDto;
import aswemake.project.domain.member.entity.request.LoginMemberRequestDto;
import aswemake.project.domain.member.service.MemberService;
import aswemake.project.global.config.SecurityConfig;
import aswemake.project.global.jwt.JwtAuthorizationFilter;
import aswemake.project.global.jwt.JwtToken;
import aswemake.project.global.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@Import({
        SecurityConfig.class,
        JwtAuthorizationFilter.class,
        JwtTokenProvider.class}
        )
@Slf4j
class MemberControllerTest {
    @Autowired
    protected MemberController memberController;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected MemberService memberService;

    @Test
    @DisplayName("회원가입 요청 실패 - 이메일에 @를 포함하지 않아서 실패")
    void join1() throws Exception{
        JoinMemberRequestDto request = JoinMemberRequestDto.builder()
                .email("abcd1234").password("123456789").build();

        MvcResult result = mockMvc.perform(
                        post("/api/v1/member/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();// 400 Bad Request

        String message = result.getResolvedException().getMessage();
        Assertions.assertThat(message).contains("이메일은 '@'를 반드시 포함해야 합니다.");
    }

    @Test
    @DisplayName("회원가입 요청 실패 - 비밀번호가 8자리 미만이라서 실패")
    void join2() throws Exception{
        JoinMemberRequestDto request = JoinMemberRequestDto.builder()
                .email("abcd@1234").password("1234").build();

        MvcResult result = mockMvc.perform(
                        post("/api/v1/member/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();// 400 Bad Request

        String message = result.getResolvedException().getMessage();
        log.debug("errorMessage : {} ", message);
        Assertions.assertThat(message).contains("비밀번호는 8자 이상 입력해주세요.");
    }

    @Test
    @DisplayName("회원가입 성공")
    void join3() throws Exception{
        JoinMemberRequestDto request = JoinMemberRequestDto.builder()
                .email("abcd@1234").password("123456789").build();

        MvcResult result = mockMvc.perform(
                        post("/api/v1/member/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        verify(memberService).join(any(JoinMemberRequestDto.class));
    }

    @Test
    @DisplayName("로그인 요청 실패 - 이메일에 @를 포함하지 않아서 실패")
    void login1() throws Exception{
        LoginMemberRequestDto request = LoginMemberRequestDto.builder()
                .email("abcd1234").password("123456789").build();

        MvcResult result = mockMvc.perform(
                        post("/api/v1/member/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();// 400 Bad Request

        String message = result.getResolvedException().getMessage();
        log.debug("errorMessage : {} ", message);
        Assertions.assertThat(message).contains("이메일은 '@'를 반드시 포함해야 합니다.");
    }

    @Test
    @DisplayName("로그인 요청 실패 - 비밀번호가 8자리 미만이라서 실패")
    void login2() throws Exception{
        LoginMemberRequestDto request = LoginMemberRequestDto.builder()
                .email("abcd@1234").password("1234").build();

        MvcResult result = mockMvc.perform(
                        post("/api/v1/member/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();// 400 Bad Request

        String message = result.getResolvedException().getMessage();
        log.debug("errorMessage : {} ", message);
        Assertions.assertThat(message).contains("비밀번호는 8자 이상 입력해주세요.");
    }

    @Test
    @DisplayName("로그인 요청 성공.")
    void login() throws Exception {
        //given
        LoginMemberRequestDto request = LoginMemberRequestDto.builder()
                .email("abcd@1234").password("123456789").build();
        JwtToken token = JwtToken.builder()
                .accessToken("accessToken_test")
                .build();

        //올바른 형식의 LoginMemberRequest를 받아 성공적으로 JwtToken 반환
        given(memberService.login(any(LoginMemberRequestDto.class))).willReturn(token);

        // When, then
        ResultActions result = mockMvc
                .perform(
                        post("/api/v1/member/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                ;

        String accessToken = result.andReturn().getResponse().getHeader("Authentication");
        Assertions.assertThat(accessToken).isNotNull();
    }
}