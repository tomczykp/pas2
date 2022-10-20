package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "accountId")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String number;
    @Column
    private double balance;
    @Column
    private double percentage;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;
    @ManyToOne
    @NotNull
    private Client owner;
    @Column
    private boolean active = true;
    @Version
    private Integer version;

    public Account() {}

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("number", number)
                .append("balance", balance)
                .append("percentage", percentage)
                .append("accountType", accountType)
                .append("owner", owner)
                .append("active", active)
                .toString();
    }

    public String getNumber() {
        return this.number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Client getOwner() {
        return owner;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) throws Exception {
        if (getBalance() >= amount) {
            this.balance = (this.getBalance() - amount);
        } else {
            throw new Exception("Brak pieniędzy na koncie do wypłaty ");
        }
    }

    public Account(double percentage, AccountType accountType, Client owner) throws Exception {
        this.balance = 0;
        this.percentage = percentage;
        this.accountType = accountType;
        this.owner = owner;

        if(percentage < 0) {
            throw new Exception("Oprocentowanie nie może być ujemne!");
        }
        if(owner == null) {
            throw new Exception("Nieokreslony wlasciciel!");
        }
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;

        if (!(o instanceof Account account)) return false;

        return new EqualsBuilder().append(getBalance(), account.getBalance()).append(getPercentage(),
                account.getPercentage()).append(isActive(), account.isActive()).append(getNumber(),
                account.getNumber()).append(getAccountType(), account.getAccountType()).append(getOwner(),
                account.getOwner()).isEquals();
    }

    @Override
    public int hashCode () {
        return new HashCodeBuilder(17, 37).append(getNumber()).append(getBalance()).append(getPercentage()).append(getAccountType()).append(getOwner()).append(isActive()).toHashCode();
    }

}


