package net.chaimae.transactionservice.integration;

import java.util.UUID;

public record AccountDto(UUID id, String customerReference, String status, String currency, double balance) {
}
