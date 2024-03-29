package com.google.bep.account.controller;

import com.google.bep.account.domain.model.Account;
import com.google.bep.account.dto.RequestAccountDTO;
import com.google.bep.account.dto.RequestLoginDTO;
import com.google.bep.jwt.domain.UserDetailsImpl;
import com.google.bep.jwt.token.TokenDTO;
import com.google.bep.error.dto.ResponseError;
import com.google.bep.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sign Up & Login", description = "회원 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "소셜 로그인", description = "회원이 아니면 회원가입과 동시에 JWT 토큰 발급, 회원이면 JWT 토큰 새로 발급")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenDTO.class)))
    @PostMapping("/login/google")
    public ResponseEntity googleSignupAndLogin(@RequestBody RequestAccountDTO accountDTO) {
        accountService.signUp(accountDTO);
        TokenDTO authToken = accountService.login(accountDTO.getEmail(), accountDTO.getPassword());
        return ResponseEntity.ok(authToken);
    }

    @Operation(summary = "회원가입", description = "기본 회원가입입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 완료", content = @Content),
            @ApiResponse(responseCode = "400", description = "Provider가 google 혹은 bep가 아닐 경우", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RequestAccountDTO accountDTO) {
        accountService.signUp(accountDTO);          // 회원가입 정보 저장
        return ResponseEntity.ok("회원가입 완료");
    }

    @Operation(summary = "로그인", description = "기본 로그인입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호를 잘못 입력했습니다.", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody RequestLoginDTO requestLoginDTO) {
        TokenDTO authToken = accountService.login(requestLoginDTO.getEmail(), requestLoginDTO.getPassword());
        return ResponseEntity.ok(authToken);
    }

    @Operation(summary = "유저 포인트", description = "현재 유저의 포인트를 가져오는 api입니다.")
    @ApiResponse(responseCode = "200", description = "호출 성공")
    @GetMapping("/user-point")
    public ResponseEntity<Integer> getUserPoint(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Account account = userDetails.getAccount();
        Integer userPoint = accountService.getUserPoint(account);
        return ResponseEntity.ok(userPoint);
    }
}
