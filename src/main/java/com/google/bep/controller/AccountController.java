package com.google.bep.controller;

import com.google.bep.domain.model.Account;
import com.google.bep.dto.RequestLoginDTO;
import com.google.bep.dto.RequestAccountDTO;
import com.google.bep.dto.TokenDTO;
import com.google.bep.dto.ErrorResult;
import com.google.bep.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/login/google")
    public ResponseEntity<TokenDTO> GoogleSignupAndLogin(@RequestBody RequestAccountDTO accountDTO) throws Exception {
        Account account = accountService.signUp(accountDTO);
        TokenDTO authToken = accountService.login(account.getEmail(), account.getPassword());
        return ResponseEntity.ok(authToken);
    }

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
