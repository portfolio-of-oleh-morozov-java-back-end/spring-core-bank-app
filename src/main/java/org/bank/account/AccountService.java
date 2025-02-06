package org.bank.account;

import org.bank.user.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {

    private final Map<Integer, Account> accountMap;
    private int idCounter;
    private final AccountProperties accountProperties;

    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
        this.accountMap = new HashMap<>();
        this.idCounter = 0;
    }

    public synchronized Account createAccount(User user) {
        idCounter++;
        Account account = new Account(idCounter, user.getId(), accountProperties.getDefaultAccountAmount());
        accountMap.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findAccountById(int id) {
        return Optional.ofNullable(accountMap.get(id));
    }

    public List<Account> getAllUserAccounts(int userId) {
        return accountMap.values()
                .stream()
                .filter(account -> account.getUserId() == userId)
                .toList();
    }

    public void depositAccount(int accountId, int moneyToDeposit) {
        Account account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));
        if (moneyToDeposit <= 0) {
            throw new IllegalArgumentException("Money to deposit must be positive: amount=%s"
                    .formatted(moneyToDeposit));
        }
        account.setBalance(account.getBalance() + moneyToDeposit);
    }

    public void withdrawFromAccount(int accountId, int amountToWithdraw) {
        Account account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));

        if (amountToWithdraw <= 0) {
            throw new IllegalArgumentException("Amount to withdraw must be positive: amount=%s"
                    .formatted(amountToWithdraw));
        }

        if (account.getBalance() < amountToWithdraw) {
            throw new IllegalArgumentException("Cannot withdraw from account: id=%s, amount=%s, attemptedWithdraw=%s"
                    .formatted(accountId, account.getBalance(), amountToWithdraw));
        }

        account.setBalance(account.getBalance() - amountToWithdraw);
    }

    public Account closeAccount(int accountId) {
        Account accountToRewove = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));
        List<Account> accountList = getAllUserAccounts(accountToRewove.getUserId());
        if (accountList.size() == 1) {
            throw new IllegalArgumentException("Cannot close the only one account");
        }
        Account accountToDeposit = accountList.stream()
                .filter(it -> it.getId() != accountId)
                .findFirst()
                .orElseThrow();

        accountToDeposit.setBalance(accountToDeposit.getBalance() + accountToRewove.getBalance());
        accountMap.remove(accountId);
        return accountToRewove;
    }

    public void transfer(int fromAccountId, int toAccountId, int amountToTransfer) {
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to the same account: id=%s".formatted(fromAccountId));
        }

        Account accountFrom = findAccountById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("No such source account: id=%s".formatted(fromAccountId)));

        Account accountTo = findAccountById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("No such destination account: id=%s".formatted(toAccountId)));

        if (amountToTransfer <= 0) {
            throw new IllegalArgumentException("Amount to transfer must be positive: amount=%s".formatted(amountToTransfer));
        }

        if (accountFrom.getBalance() < amountToTransfer) {
            throw new IllegalArgumentException("Insufficient funds in source account: id=%s, balance=%s, attemptedTransfer=%s"
                    .formatted(accountFrom, accountFrom.getBalance(), amountToTransfer));
        }

        int totalAmountToDeposit = accountTo.getUserId() != accountFrom.getUserId()
                ? (int) (amountToTransfer * (1 - accountProperties.getTransferCommission()))
                : amountToTransfer;

        accountFrom.setBalance(accountFrom.getBalance() - amountToTransfer);
        accountTo.setBalance(accountTo.getBalance() + totalAmountToDeposit);
    }

}
