package net.chaimae.transactionservice.integration;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "${account-service.url:}", path = "/api/accounts")
public interface AccountClient {

    @GetMapping("/{id}")
    AccountDto getAccount(@PathVariable UUID id);

    @PatchMapping("/{id}/balance")
    AccountDto updateBalance(@PathVariable UUID id, @RequestBody BalanceUpdateRequest request);
}
