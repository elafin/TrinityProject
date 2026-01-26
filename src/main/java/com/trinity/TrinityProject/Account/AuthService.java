package com.trinity.TrinityProject.Account;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Transactional
    public void register(RegisterRequest req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("Hasła nie są takie same.");
        }
        if (accountRepository.existsByLogin(req.getLogin())) {
            throw new IllegalArgumentException("Taki login już istnieje.");
        }

        Account a = new Account();
        a.setLogin(req.getLogin());
        a.setPassword(passwordEncoder.encode(req.getPassword())); // zapisujemy HASH

        accountRepository.save(a);
    }
}
