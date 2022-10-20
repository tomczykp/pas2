package app.actions;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import app.model.Account;
import app.model.AccountType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transferId;
    @NotNull
    @ManyToOne(cascade=CascadeType.MERGE)
    private Account toAccount;
    @NotNull
    @ManyToOne(cascade=CascadeType.MERGE)
    private Account fromAccount;
    @Column
    private double amount;
    @Column
    private LocalDateTime transferDate;
    @Version
    private long version;

    public Transfer() {}

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) throws Exception {
        if ( transferDate.getSecond() < LocalDateTime.now().getSecond())
            throw new Exception("Data nie powinna być w przeszłości ");
        this.transferDate = transferDate;
    }

    public int getTransferId() {
        return this.transferId;
    }
    public Account getToAccount() {
        return toAccount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public double getAmount() {
        return amount;
    }

    public Transfer(Account toAccount, Account fromAccount, double amount, LocalDateTime transferDate) throws Exception {
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
        this.amount = amount;
        this.transferDate = transferDate;

        if(!fromAccount.isActive() || !toAccount.isActive()) throw new Exception("Konto jest nieaktywne");
        if((fromAccount.getAccountType() == AccountType.Savings) && (fromAccount.getOwner()!= toAccount.getOwner()))
            throw new Exception("Z konta oszczednosciowego nie mozna robic przelewow do nie swoich kont!");
        if(amount<0)
            throw new Exception("Wartosc przelewu musi byc dodatnia!");
        this.fromAccount.withdraw(this.amount);
        this.toAccount.deposit(this.amount);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("toAccount", toAccount)
                .append("fromAccount", fromAccount)
                .append("amount", amount)
                .append("transferDate", transferDate)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Transfer)) return false;

        Transfer transfer = (Transfer) o;

        return new EqualsBuilder().append(getTransferId(), transfer.getTransferId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getTransferId()).toHashCode();
    }
}
