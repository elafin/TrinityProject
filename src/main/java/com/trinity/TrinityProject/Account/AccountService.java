package com.trinity.TrinityProject.Account;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository repo;

    public AccountService(AccountRepository repo) { this.repo = repo; }

    public Account getByLogin(String login) {
        return repo.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Brak konta: " + login));
    }
}
