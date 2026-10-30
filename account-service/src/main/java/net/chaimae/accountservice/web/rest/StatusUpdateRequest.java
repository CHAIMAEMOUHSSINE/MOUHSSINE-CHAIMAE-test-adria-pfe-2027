package net.chaimae.accountservice.web.rest;

import jakarta.validation.constraints.NotNull;
import net.chaimae.accountservice.Account.AccountStatus;

public record StatusUpdateRequest(@NotNull AccountStatus status) {
}
