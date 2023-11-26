package by.fin.internetbanking.entity;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "user_operation")
public class UserOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, name = "created_date")
    private ZonedDateTime createdDate;

    @Column(nullable = false, name = "user_id")
//    @OneToMany
//    Todo: make correct relationship between 2 tables.
    private Long userId;

    @Column(nullable = false, name = "operation")
    @Enumerated(EnumType.STRING)
    private OperationType operation;

    @Column(nullable = false, name = "amount_of_money")
    private BigDecimal amountOfMoney;

    public UserOperation() {
    }

    public UserOperation(Long userId, OperationType operation, BigDecimal amountOfMoney) {
        this.userId = userId;
        this.operation = operation;
        this.amountOfMoney = amountOfMoney;
    }

    public enum OperationType {
        ADD, TAKE
    }

}
