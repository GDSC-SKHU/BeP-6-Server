package com.google.bep.jwt.service;

import com.google.bep.account.domain.model.Account;
import com.google.bep.account.domain.repository.AccountRepository;
import com.google.bep.error.exception.RestApiException;
import com.google.bep.jwt.domain.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.google.bep.error.errorcode.CustomErrorCode.UNAUTHORIZED;


@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        Account findAccount = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(UNAUTHORIZED));

        if (findAccount != null) {
            UserDetailsImpl userDetails = new UserDetailsImpl(findAccount);
            return userDetails;
        }

        return null;
    }
}
