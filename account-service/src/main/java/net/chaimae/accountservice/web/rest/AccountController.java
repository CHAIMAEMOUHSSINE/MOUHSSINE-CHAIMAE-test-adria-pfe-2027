package net.chaimae.accountservice.web.rest;



import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import net.chaimae.accountservice.Account.Account;
import net.chaimae.accountservice.service.AccountService;
import net.chaimae.accountservice.service.CreateAccountCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public List<Account> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Account get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@Valid @RequestBody CreateAccountCommand cmd) {
        return service.create(cmd);
    }

    @PatchMapping("/{id}/status")
    public Account changeStatus(@PathVariable UUID id, @Valid @RequestBody StatusUpdateRequest request) {
        return service.changeStatus(id, request.status());
    }

    /** Utilisé par le transaction-service (OpenFeign) pour débiter / créditer. */
    @PatchMapping("/{id}/balance")
    public Account updateBalance(@PathVariable UUID id, @RequestBody BalanceUpdateRequest request) {
        return service.applyBalanceDelta(id, request.delta());
    }
}
