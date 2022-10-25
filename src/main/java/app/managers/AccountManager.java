package app.managers;


import app.model.Account;
import app.model.AccountType;
import app.model.Client;
import app.repository.InMemoryRepository;
import app.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class AccountManager {

    private final Repository<String, Account> repository;
    private final String accountIdGenerator = "0";

    public AccountManager() {
        this.repository = new InMemoryRepository<>();
    }

    public Account createAccount(double percentage, AccountType accountType, Client owner) throws Exception {

        Account newAccount = new Account(percentage, accountType, owner);
        String accountId = accountIdGenerator + repository.getLenght();
        return this.repository.insert(accountId, newAccount);
    }

    public HashMap<String, Account> getMap() {
        return this.repository.getMap();
    }

    public void deleteAccount(String id) {
        this.repository.delete(id);
    }

    public Account get(String id) {
        return this.repository.get(id);
    }

    public List<Account> get (Predicate<Account> predicate) {
        try {
            return this.repository.get(predicate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void withdraw(double val, String id) throws Exception {
        this.repository.modify(id, (account) -> {
            try {
                account.withdraw(val);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return account;
        });
    }

    public void deposit(double val, String id) throws Exception {
        this.repository.modify(id, (account) -> {
            account.deposit(val);
            return account;
        });
    }
}
