package by.fin.internetbanking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "balance")
    private BigDecimal balance;

    public int removeMoney(BigDecimal moneyAmount) {
        if (this.balance.compareTo(moneyAmount) == 1) {
            this.balance = this.balance.subtract(moneyAmount);
//            System.out.println("we are here and new balance is " + this.balance);
            return 1;
        }
        return 0;
    }

    public int addMoney(BigDecimal moneyAmount) {
        try {
            this.balance = this.balance.add(moneyAmount);
//            System.out.println("ADD and new balance is " + this.balance);
        }catch (Exception e) {
            return 0;
        }
        return 1;
    }

    public UserBalance() {
    }

    public UserBalance(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}
