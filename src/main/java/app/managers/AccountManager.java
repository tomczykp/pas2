package app.managers;


import app.model.Account;
import app.model.AccountType;
import app.model.Client;
import app.repository.InMemoryRepository;
import app.repository.Repository;

import java.util.HashMap;
import java.util.function.Predicate;

public class AccountManager {

    private Repository<String, Account> repository;
    private String accountIdGenerator = "0";

    public AccountManager() {
        this.repository = new InMemoryRepository<>();
    }

    public Account createAccount(double percentage, AccountType accountType, Client owner){
        try {
            Account newAccount = new Account(percentage, accountType, owner);
            String accountId = accountIdGenerator + repository.getLenght();
            this.repository.insert(accountId, newAccount);
            return newAccount;
        } catch (Exception e) {
            System.out.println("Could not create Account");
        }
        return null;
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

    public Account get(Predicate<Account> predicate) {
        try {
            return this.repository.get(predicate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void withdraw(double val, String id) {
        try {
            this.get(id).withdraw(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deposit(double val, String id) {
        this.get(id).deposit(val);
    }
}
