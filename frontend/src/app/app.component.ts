import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Account {
  id: string;
  customerReference: string;
  status: string;
  currency: string;
  balance: number;
}

interface Transaction {
  id: string;
  accountId: string;
  amount: number;
  currency: string;
  merchantCategory: string;
  country: string;
  createdAt: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly http = inject(HttpClient);
  private readonly formBuilder = inject(FormBuilder);
  readonly transactionForm = this.formBuilder.nonNullable.group({
    accountId: ['', Validators.required],
    amount: [0.01, [Validators.required, Validators.min(0.01)]],
    currency: ['EUR', Validators.required],
    merchantCategory: ['GENERAL', Validators.required],
    country: ['MA', Validators.required]
  });
  accounts: Account[] = [];
  transactions: Transaction[] = [];
  message = '';

  constructor() {
    this.loadAccounts();
    this.loadTransactions();
  }

  loadAccounts(): void {
    this.http.get<Account[]>('/api/accounts').subscribe({
      next: (accounts) => this.accounts = accounts,
      error: () => this.message = 'Impossible de charger les comptes.'
    });
  }

  loadTransactions(): void {
    this.http.get<Transaction[]>('/api/transactions').subscribe({
      next: (transactions) => this.transactions = transactions,
      error: () => this.message = 'Impossible de charger les transactions.'
    });
  }

  createTransaction(): void {
    if (this.transactionForm.invalid) {
      this.transactionForm.markAllAsTouched();
      return;
    }
    this.http.post<Transaction>('/api/transactions', this.transactionForm.getRawValue()).subscribe({
      next: () => {
        this.message = 'Transaction enregistrée.';
        this.loadAccounts();
        this.loadTransactions();
      },
      error: (error: { error?: { detail?: string } }) =>
        this.message = error.error?.detail ?? 'La transaction a été refusée.'
    });
  }
}
