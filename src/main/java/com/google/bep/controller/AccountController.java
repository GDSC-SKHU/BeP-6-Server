package com.google.bep.controller;

import com.google.bep.domain.model.Account;
import com.google.bep.dto.RequestLoginDTO;
import com.google.bep.dto.RequestAccountDTO;
import com.google.bep.dto.TokenDTO;
import com.google.bep.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity signUp(@RequestBody RequestAccountDTO accountDTO) throws Exception {
        accountService.signUp(accountDTO);          // 회원가입 정보 저장

        return ResponseEntity
                .ok().build();           // 로그인 페이지 리다이렉트
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody RequestLoginDTO requestLoginDTO) {
        TokenDTO authToken = accountService.login(requestLoginDTO.getEmail(), requestLoginDTO.getPassword());
        return ResponseEntity.ok(authToken);
    }

}
