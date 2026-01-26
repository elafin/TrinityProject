package com.trinity.TrinityProject.Account;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public DbUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accountRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie ma takiego użytkownika"));


        return new org.springframework.security.core.userdetails.User(
                acc.getLogin(),
                acc.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
