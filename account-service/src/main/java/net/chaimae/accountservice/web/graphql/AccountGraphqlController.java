package net.chaimae.accountservice.web.graphql;



import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import net.chaimae.accountservice.Account.Account;
import net.chaimae.accountservice.Account.AccountStatus;
import net.chaimae.accountservice.service.AccountService;
import net.chaimae.accountservice.service.CreateAccountCommand;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AccountGraphqlController {

    private final AccountService service;

    public AccountGraphqlController(AccountService service) {
        this.service = service;
    }

    @QueryMapping
    public List<Account> accounts() {
        return service.findAll();
    }

    @QueryMapping
    public Account account(@Argument UUID id) {
        return service.get(id);
    }

    @MutationMapping
    public Account createAccount(@Argument @Valid CreateAccountCommand input) {
        return service.create(input);
    }

    @MutationMapping
    public Account updateAccountStatus(@Argument UUID id, @Argument AccountStatus status) {
        return service.changeStatus(id, status);
    }
}

