package com.google.bep.service;

import com.google.bep.domain.model.Account;
import com.google.bep.domain.repository.AccountRepository;
import com.google.bep.dto.RequestAccountDTO;
import com.google.bep.dto.TokenDTO;
import com.google.bep.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Account signUp(RequestAccountDTO accountDTO) throws Exception {
        Account exitingAccount = accountRepository.findByEmail(accountDTO.getEmail()).orElse(null);

        if (!accountDTO.getProvider().equals("google") && exitingAccount != null ) {
            throw new Exception();
        }

        // 유저 정보가 없을 경우
        if (exitingAccount == null) {
            List<String> roles = new ArrayList<>();
            roles.add("ROLE_USER");
            exitingAccount = accountRepository.save(Account.builder()
                    .email(accountDTO.getEmail())
                    .name(accountDTO.getName())
                    .password(passwordEncoder.encode(accountDTO.getPassword()))
                    .roles(roles)
                    .userPoint(0)
                    .provider(accountDTO.getProvider())
                    .build());
            }

        return exitingAccount;
    }

    @Transactional
    public TokenDTO login(String email, String password) {

        // 1. ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 객체는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);


        // 2. 실제 검증(사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 JwtUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증된 정보를 기반으로 JWT 토큰 생성
        TokenDTO tokenDTO = tokenProvider.createToken(authentication);
        tokenDTO.setUserPoint(accountRepository.findByEmail(email).orElse(null).getUserPoint());        // 유저포인트 세팅
        // jwt 토큰 생성
        return tokenDTO;
    }

}
