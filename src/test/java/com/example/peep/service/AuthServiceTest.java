package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.details.SmsUtil;
import com.example.peep.domain.LoginRecord;
import com.example.peep.domain.RefreshToken;
import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.request.VerifyCodeRequestDto;
import com.example.peep.repository.LoginRecordRepository;
import com.example.peep.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private LoginRecordRepository loginRecordRepository;
    @Mock private TokenBlacklistService tokenBlacklistService;
    @Mock private VerificationCodeService verificationCodeService;
    @Mock private SmsUtil smsUtil;

    @InjectMocks private AuthService authService;

    void setUp() {
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
    }

    @DisplayName("login - 로그인 테스트")
    @Test
    void given_whenLogin_thenReturnJwtTokenDto() {

        //Given
        setUp();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("Device-Id", "device123");
        request.setParameter("User-Agent", "deviceInfo");
        StudentDto studentDto = StudentDto.of("jihoon", "1234", "지훈", null, 0, 0, null);

        Authentication authentication = Mockito.mock(Authentication.class);

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        JwtToken expectedToken = JwtToken.of("Bearer", "accessToken", "refreshToken", "jihoon");

        given(jwtTokenProvider.generateToken(authentication, studentDto.userId(), request.getHeader("Method...")))
                .willReturn(expectedToken);
        //When
        JwtTokenDto result = authService.login(request, studentDto);
        //Then
        ArgumentCaptor<LoginRecord> studentCaptor = ArgumentCaptor.forClass(LoginRecord.class);
        then(loginRecordRepository).should().save(studentCaptor.capture());
        assertThat(result).isNotNull();
    }

    @DisplayName("refreshToken - 토큰 갱신 테스트")
    @Test
    void givenRefreshToken_whenRefreshAccessToken_thenReturnNewAccessToken() {

        //Given
        RefreshToken refreshToken = RefreshToken.of("Bearer", "accessToken", "refreshToken", new Date());
        JwtTokenDto token = JwtTokenDto.of("Bearer", "accessToken", "refreshToken", "jihoon");
        given(refreshTokenRepository.findByUserIdAndDeviceId("jihoon", "UUID")).willReturn(refreshToken);
        given(jwtTokenProvider.validateToken("refreshToken")).willReturn(true);
        given(jwtTokenProvider.generateAccess("jihoon")).willReturn("newAccessToken");
        //When
        JwtTokenDto newToken = authService.refreshToken("UUID", token, "oldAccess");

        //Then
        then(tokenBlacklistService).should().addToBlacklist("oldAccess");
        assertThat(newToken.accessToken()).isEqualTo("newAccessToken");
    }

    @DisplayName("logout - 로그아웃테스트")
    @Test
    void givenJwtToken_whenLogout_thenDeleteRefreshToken() {

        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("Device_Id", "UUID");
        request.setParameter("User-Agent", "deviceInfo");
        JwtTokenDto jwtTokenDto = JwtTokenDto.of("Bearer", "accessToken", "refreshToken", "jihoon");

        //When
        authService.logout(request, jwtTokenDto,"oldAccess");

        //Then
        ArgumentCaptor<LoginRecord> studentCaptor = ArgumentCaptor.forClass(LoginRecord.class);
        then(loginRecordRepository).should().save(studentCaptor.capture());
        then(tokenBlacklistService).should().addToBlacklist("oldAccess");
        then(refreshTokenRepository).should().deleteByUserIdAndToken("jihoon", "refreshToken");
    }

    @DisplayName("sendSms - 인증번호 전송")
    @Test
    void givenPhoneNumber_whenSendSms_thenSendVerifyCode() {

        //Given
        String phone = "01012345678";
        

        //When
        authService.sendSms(phone);
        //Then
        then(smsUtil).should().sendOne(eq(phone), any());
        then(verificationCodeService).should().saveVerificationCode(eq(phone), any());

    }

    @DisplayName("checkVerifyCode - 인증번호 확인")
    @Test
    void givenVerifyCode_whenCheckVerifyCode_thenResult() {

        //Given
        VerifyCodeRequestDto verifyCodeRequestDto = VerifyCodeRequestDto.of("01012345678", "123456");
        given(verificationCodeService.verifyCode("01012345678", "123456")).willReturn(true);
        //When then
        assertThat(authService.checkVerifyCode(verifyCodeRequestDto)).isEqualTo(true);
    }


}

