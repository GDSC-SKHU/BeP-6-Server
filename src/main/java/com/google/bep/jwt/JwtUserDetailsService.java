package com.google.bep.jwt;

import com.google.bep.domain.model.Account;
import com.google.bep.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email을 가진 유저를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Account account) {
        return account.builder()
                .email(account.getEmail())
                .name(account.getName())
                .password(passwordEncoder.encode(account.getPassword()))
                .roles(account.getRoles())
                .build();
    }
}
