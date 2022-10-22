package app.actions;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import app.model.Account;
import app.model.AccountType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan")
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;
    @Version
    private long version;
    @Column
    private double initAmount;
    @Column
    private double percentage;
    @Column
    @NotNull
    private LocalDateTime beginDate;
    @Column
    @NotNull
    private LocalDateTime endDate;
    @Column
    private double paidAmount = 0;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "accountId")
    @NotNull
    private Account account;
    @Column
    private boolean archive = false;

    public Loan(Account acc, double amo, double perc, LocalDateTime eDate, LocalDateTime bDate) throws Exception {
        bDate = LocalDateTime.now();
        initAmount = amo;
        account = acc;
        percentage = perc;
        beginDate = bDate;
        endDate = eDate;

       if(acc.getAccountType() == AccountType.Savings) {
            throw new Exception("Nie mozna wziac kredytu na konto oszczednosciowe!");
       }
        acc.deposit(amo);
    }

    public Loan() {}

    public int getLoanId() {
        return this.loanId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

   public LocalDateTime getEndDate() {
        return endDate;
   }

   public void setEndDate(LocalDateTime date) throws Exception {
        if (date.getSecond() > getBeginDate().getSecond()) {
            endDate = date;
        } else {
            throw new Exception("Zła data");
        }
   }

   public double getInitAmount() {
        return this.initAmount;
   }

   public double getPaidAmount() {
        return this.paidAmount;
   }

   public double getPercentage() {
        return percentage;
   }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("initAmount", initAmount)
                .append("percentage", percentage)
                .append("beginDate", beginDate)
                .append("endDate", endDate)
                .append("paidAmount", paidAmount)
                .append("account", account)
                .append("archive", archive)
                .toString();
    }

    private double toRepay() {
        if(isArchive()) return 0;
        return getInitAmount()+(getInitAmount()*0.01*getPercentage())-getPaidAmount();
    }


    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public void repay(double amount, LocalDateTime date) throws Exception {
        if( !this.isArchive()) {
            this.account.withdraw(amount);

            if(isOverdue(date)) {
                paidAmount = (paidAmount + amount - overdueAmount());
                setEndDate(date.plusHours(720));
            }
            else paidAmount = paidAmount + amount;

            if(toRepay() <= 0) {
                account.deposit((-1)*toRepay());
                setArchive(true);
            }
        }
        else {
            throw new Exception("Nie można spłacić pożyczki już spłaconej");
        }
    }

    public boolean isOverdue(LocalDateTime date) {
        return date.getSecond() > getEndDate().getSecond();
    }

    private double overdueAmount() {
        return 0.25*toRepay();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Loan loan)) return false;

        return new EqualsBuilder().append(getLoanId(), loan.getLoanId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getLoanId()).toHashCode();
    }
}
