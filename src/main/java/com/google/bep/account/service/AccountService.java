package com.google.bep.account.service;

import com.google.bep.account.domain.model.Account;
import com.google.bep.account.domain.model.Role;
import com.google.bep.account.domain.repository.AccountRepository;
import com.google.bep.account.dto.RequestAccountDTO;
import com.google.bep.error.exception.RestApiException;
import com.google.bep.jwt.service.JwtUserDetailsService;
import com.google.bep.jwt.token.TokenDTO;
import com.google.bep.jwt.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.google.bep.error.errorcode.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserDetailsService jwtUserDetailsService;

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new RestApiException(ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Account signUp(RequestAccountDTO accountDTO) {
        Account exitingAccount = accountRepository.findByEmail(accountDTO.getEmail()).orElse(null);

        if (!accountDTO.getProvider().equals("google") && exitingAccount != null ) {
            throw new RestApiException(DUPLICATE_RESOURCE);
        }

        if (!accountDTO.getProvider().equals("google") && !accountDTO.getProvider().equals("bep")) {
            throw new RestApiException(INVALID_PROVIDER);
        }

        // 유저 정보가 없을 경우 가입
        if (exitingAccount == null) {
            exitingAccount = accountRepository.save(Account.builder()
                    .email(accountDTO.getEmail())
                    .name(accountDTO.getName())
                    .password(passwordEncoder.encode(accountDTO.getPassword()))
                    .role(Role.USER)
                    .userPoint(0)
                    .provider(accountDTO.getProvider())
                    .build());
            }

        return exitingAccount;
    }

    @Transactional
    public TokenDTO login(String email, String password) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);

        // 1. ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 객체는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password, userDetails.getAuthorities());

        // 2. 실제 검증(사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 JwtUserDetailsService에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증된 정보를 기반으로 JWT 토큰 생성
        TokenDTO tokenDTO = tokenProvider.createToken(authentication.getName(), getAuthorities(authentication));

        tokenDTO.setUserPoint(findByEmail(email).getUserPoint());        // 유저포인트 세팅
        // jwt 토큰 생성
        return tokenDTO;
    }

    // 권한 이름 가져오기
    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public Integer getUserPoint(Account account) {
        return account.getUserPoint();
    }

    public void updateUserPoint(Account account, String op, int point) {
        account.updateUserPoint(op, point);
        accountRepository.save(account);
    }
}
