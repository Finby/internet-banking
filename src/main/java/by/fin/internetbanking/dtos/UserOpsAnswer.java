package by.fin.internetbanking.dtos;

import by.fin.internetbanking.entity.UserOperation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class UserOpsAnswer {
    public ZonedDateTime operationDate;
    public UserOperation.OperationType operationType;
    public BigDecimal amountMoney;
}
