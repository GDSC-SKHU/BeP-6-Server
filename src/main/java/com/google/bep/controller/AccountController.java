package com.google.bep.controller;

import com.google.bep.domain.model.Account;
import com.google.bep.dto.ErrorResult;
import com.google.bep.dto.RequestAccountDTO;
import com.google.bep.dto.RequestLoginDTO;
import com.google.bep.dto.TokenDTO;
import com.google.bep.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sign Up & Login", description = "회원 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "소셜 로그인", description = "회원이 아니면 회원가입과 동시에 JWT 토큰 발급, 회원이면 JWT 토큰 새로 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Provider Error", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("/login/google")
    public ResponseEntity GoogleSignupAndLogin(@RequestBody RequestAccountDTO accountDTO) {
        try {
            Account account = accountService.signUp(accountDTO);
            TokenDTO authToken = accountService.login(accountDTO.getEmail(), accountDTO.getPassword());
            return ResponseEntity.ok(authToken);
        } catch (Exception e) {
            ErrorResult errorResult = new ErrorResult(409, "CONFLICT", "Provider Error");
            return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "회원가입", description = "기본 회원가입입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 완료", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody RequestAccountDTO accountDTO) {
        try {
            accountService.signUp(accountDTO);          // 회원가입 정보 저장
            return ResponseEntity
                    .ok("회원가입 완료");           // 로그인 페이지 리다이렉트
        }
        catch (Exception e) {
            ErrorResult errorResult = new ErrorResult(409, "CONFLICT", "이미 존재하는 이메일입니다.");
            return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
        }
    }


    @Operation(summary = "로그인", description = "기본 로그인입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호를 잘못 입력했습니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody RequestLoginDTO requestLoginDTO) {
        try {
            TokenDTO authToken = accountService.login(requestLoginDTO.getEmail(), requestLoginDTO.getPassword());
            return ResponseEntity.ok(authToken);
        } catch (BadCredentialsException e) {
            ErrorResult errorResult = new ErrorResult(401, "UNAUTHORIZED", "아이디 또는 비밀번호를 잘못 입력했습니다.");
            return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
        }
    }
}
