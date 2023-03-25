package com.google.bep.jwt;

import com.google.bep.domain.model.Account;
import com.google.bep.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(""));
    }

    private UserDetails createUserDetails(Account account) {
        return account.builder()
                .email(account.getEmail())
                .name(account.getName())
                .password(account.getPassword())
                .roles(account.getRoles())
                .build();
    }
}
