package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.details.SmsUtil;
import com.example.peep.domain.Student;
import com.example.peep.domain.enumType.Event;
import com.example.peep.domain.enumType.Gender;
import com.example.peep.dto.request.LoginRequest;
import com.example.peep.dto.response.JwtTokenResponse;
import com.example.peep.errors.ErrorCode;
import com.example.peep.errors.PeepApiException;
import com.example.peep.fixture.StudentFixture;
import com.example.peep.repository.LoginRecordRepository;
import com.example.peep.repository.RefreshTokenRepository;
import com.example.peep.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock private StudentRepository studentRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RecordService recordService;
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
        String userId = "jihoon";
        String deviceId = "device123";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Device-Id", deviceId);

        LoginRequest loginRequest = LoginRequest.of(userId, "1234");

        JwtToken token = JwtToken.of("Bearer", "accessToken", "refreshToken", userId);
        Student student = StudentFixture.getStudent(userId, "지훈", Gender.MALE);

        given(studentRepository.findByUserId(userId)).willReturn(Optional.of(student));
        given(passwordEncoder.matches(loginRequest.password(),student.getUserPassword())).willReturn(true);
        given(refreshTokenRepository.findByUserIdAndDeviceId(userId, deviceId)).willReturn(null);
        given(jwtTokenProvider.generateToken(userId, deviceId)).willReturn(token);

        //when
        JwtTokenResponse result = authService.login(request, loginRequest);

        //then
        then(recordService).should().recordEvent(any(),eq("jihoon"),eq(Event.LOG_IN));
        assertThat(result.accessToken()).isEqualTo("accessToken");
        assertThat(result.id()).isEqualTo("jihoon");

    }

    @DisplayName("login - 존재하지 않는 아이디로 로그인 요청시 실패")
    @Test
    void givenNotFoundedId_whenLogin_thenUserNotFounded() {
        //Given
        String userId = "jihoon";
        String deviceId = "device123";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Device-Id", deviceId);

        LoginRequest loginRequest = LoginRequest.of(userId, "1234");

        doThrow(new PeepApiException(ErrorCode.USER_NOT_FOUND)).when(studentRepository).findByUserId(userId);
//        when(studentRepository.findByUserId(userId)).thenThrow(new PeepApiException(ErrorCode.USER_NOT_FOUND));
        PeepApiException e = assertThrows(PeepApiException.class, () -> authService.login(request, loginRequest));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

//    @DisplayName("refreshToken - 토큰 갱신 테스트")
//    @Test
//    void givenRefreshToken_whenRefreshAccessToken_thenReturnNewAccessToken() {

        //Given
//        RefreshToken refreshToken = RefreshToken.of("Bearer", "accessToken", "refreshToken", new Date());
//        JwtTokenResponse token = JwtTokenResponse.of("Bearer", "accessToken", "refreshToken", "jihoon");
//        given(refreshTokenRepository.findByUserIdAndDeviceId("jihoon", "UUID")).willReturn(refreshToken);
//        given(jwtTokenProvider.validateToken("refreshToken")).willReturn(true);
//        given(jwtTokenProvider.generateAccess("jihoon")).willReturn("newAccessToken");
//        //When
//       JwtTokenResponse newToken = authService.refreshToken("UUID", token, "oldAccess").getBody();
//
//        //Then
//        then(tokenBlacklistService).should().addToBlacklist("oldAccess");
//        assert newToken != null;
//        assertThat(newToken.accessToken()).isEqualTo("newAccessToken");
//    }

//    @DisplayName("logout - 로그아웃테스트")
//    @Test
//    void givenJwtToken_whenLogout_thenDeleteRefreshToken() {

//        //Given
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setParameter("Device_Id", "UUID");
//        request.setParameter("User-Agent", "deviceInfo");
//        JwtTokenResponse jwtTokenDto = JwtTokenResponse.of("Bearer", "accessToken", "refreshToken", "jihoon");
//
//        //When
//        authService.logout(request, jwtTokenDto,"oldAccess");
//
//        //Then
//        ArgumentCaptor<LoginRecord> studentCaptor = ArgumentCaptor.forClass(LoginRecord.class);
//        then(loginRecordRepository).should().save(studentCaptor.capture());
//        then(tokenBlacklistService).should().addToBlacklist("oldAccess");
//        then(refreshTokenRepository).should().deleteByUserIdAndToken("jihoon", "refreshToken");
//    }

//    @DisplayName("sendSms - 인증번호 전송")
//    @Test
//    void givenPhoneNumber_whenSendSms_thenSendVerifyCode() {
//
//        //Given
//        String phone = "01012345678";
//

        //When
//        authService.sendSms(phone);
//        //Then
//        then(smsUtil).should().sendOne(eq(phone), any());
//        then(verificationCodeService).should().saveVerificationCode(eq(phone), any());
//
//    }

//    @DisplayName("checkVerifyCode - 인증번호 확인")
//    @Test
//    void givenVerifyCode_whenCheckVerifyCode_thenResult() {

//        //Given
//        VerifyCodeRequestDto verifyCodeRequestDto = VerifyCodeRequestDto.of("01012345678", "123456");
//        given(verificationCodeService.verifyCode("01012345678", "123456")).willReturn(true);
//        //When then
//        assertThat(authService.checkVerifyCode(verifyCodeRequestDto).getStatusCode()).isEqualTo(HttpStatus.OK);
//    }


}

