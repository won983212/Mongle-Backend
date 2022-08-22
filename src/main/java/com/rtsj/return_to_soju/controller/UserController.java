package com.rtsj.return_to_soju.controller;

import com.rtsj.return_to_soju.common.JwtProvider;
import com.rtsj.return_to_soju.model.dto.dto.KakaoTokenDto;
import com.rtsj.return_to_soju.model.dto.request.ReissueTokenRequestDto;
import com.rtsj.return_to_soju.model.dto.request.UserNameRequestDto;
import com.rtsj.return_to_soju.model.dto.response.LoginResponseDto;
import com.rtsj.return_to_soju.model.dto.response.ReissueTokenResponseDto;
import com.rtsj.return_to_soju.model.dto.response.SuccessResponseDto;
import com.rtsj.return_to_soju.model.dto.response.UserInfoResponseDto;
import com.rtsj.return_to_soju.repository.UserRepository;
import com.rtsj.return_to_soju.service.OauthService;
import com.rtsj.return_to_soju.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Tag(name = "USER", description = "로그인 API")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final OauthService oauthService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "로그인 API", description = "카카오 access & refresh 토큰을 사용한 회원가입 및 로그인 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
    })
    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponseDto> loginWithKakao(@RequestBody KakaoTokenDto kakaoTokenDto){
        LoginResponseDto loginResponseDto = userService.loginWithKakaoToken(kakaoTokenDto);
        return ResponseEntity.ok().body(loginResponseDto);
    }


    @Operation(summary = "이름 받기 API", description = "회원가입시 전달받은 토큰을 사용하여, 회원 이름을 등록 및 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!",
                    content = @Content(schema = @Schema(implementation = SuccessResponseDto.class)))
    })
    @PostMapping("/users/name")
    public ResponseEntity<SuccessResponseDto> loginWithKakao(HttpServletRequest request, @Valid @RequestBody UserNameRequestDto userNameDto){
        Long userId = jwtProvider.getUserIdByHeader(request);
        String userName = userNameDto.getUserName();
        userService.saveAndUpdateUserName(userId, userName);
        return ResponseEntity.ok(new SuccessResponseDto("변경이 완료되었습니다!"));
    }

    @Operation(summary = "사용자 정보 조회 API", description = "사용자 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!",
                    content = @Content(schema = @Schema(implementation = UserInfoResponseDto.class)))
    })
    @PostMapping("/users/info")
    public ResponseEntity<UserInfoResponseDto> loginWithKakao(HttpServletRequest request){
        Long userId = jwtProvider.getUserIdByHeader(request);
        UserInfoResponseDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok().body(userInfo);
    }


    @Operation(summary = "토큰재발급 API", description = "Mongle access & refresh 토큰을 사용하여 AccessToken을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!",
                    content = @Content(schema = @Schema(implementation = ReissueTokenResponseDto.class)))
    })
    @PostMapping("/reissue/token")
    public ResponseEntity<ReissueTokenResponseDto> reissueToken(@RequestBody ReissueTokenRequestDto request){
        ReissueTokenResponseDto reissueTokenResponseDto = jwtProvider.reissueToken(request);
        return ResponseEntity.ok().body(reissueTokenResponseDto);
    }

    @PostMapping("/isValid")
    public ResponseEntity<Boolean> valid(@RequestBody ReissueTokenRequestDto request){
        boolean b = jwtProvider.validateToken(request.getAccessToken());
        System.out.println("b = " + b);
        boolean b1 = jwtProvider.validateToken(request.getRefreshToken());
        System.out.println("b1 = " + b1);
        String payload = jwtProvider.getPayload(request.getAccessToken());
        System.out.println("payload = " + payload);
        String payload1 = jwtProvider.getPayload(request.getRefreshToken());
        System.out.println("payload1 = " + payload1);
        return ResponseEntity.ok().body(b);
    }

    /**
     * 로그인 요청시마다 client가 귀찮을 수도 있으니, 기존 refresh로 access 만료되면 받아서 사용하기
     */
    @PostMapping("/renew/kakao")
    public ReissueTokenResponseDto renewAccessToken(@RequestBody ReissueTokenRequestDto request){
        ReissueTokenResponseDto kakaoToken = oauthService.renewKakaoToken(request.getRefreshToken());
        return kakaoToken;
    }


}
