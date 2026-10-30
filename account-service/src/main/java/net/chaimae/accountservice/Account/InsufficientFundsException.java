package net.chaimae.accountservice.Account;


import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID id, double balance, double delta) {
        super("Solde insuffisant sur le compte " + id + " (solde " + balance + ", mouvement " + delta + ")");
    }
}
