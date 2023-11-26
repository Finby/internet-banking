package by.fin.internetbanking.service;

import by.fin.internetbanking.dtos.UserOpsAnswer;
import by.fin.internetbanking.entity.UserOperation;
import by.fin.internetbanking.repository.UserOperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserOperationService {

    private final UserOperationRepository userOperationRepository;

    public int buildAndSave(Long longUserId, UserOperation.OperationType take, BigDecimal moneyAmount) {
        UserOperation tempUserOperation = new UserOperation(longUserId, take, moneyAmount);
        userOperationRepository.save(tempUserOperation);
        return 0;
    }


    public List<UserOpsAnswer> getOperations(String userId, ZonedDateTime beginDateTime, ZonedDateTime endDateTime) {
        if (beginDateTime != null &&  endDateTime != null) {
            return userOperationRepository.findSelectedFieldsByUserIdAndDateRange(
                    Long.valueOf(userId),
                    beginDateTime,
                    endDateTime
            );
        } else if (beginDateTime != null) {
            return userOperationRepository.findSelectedFieldsByUserIdAndAfterBeginDate(Long.valueOf(userId), beginDateTime);
        } else if (endDateTime != null) {
            return userOperationRepository.findSelectedFieldsByUserIdAndBeforeEndDate(Long.valueOf(userId), endDateTime);
        }
        return userOperationRepository.findSelectedFieldsByUserId(Long.valueOf(userId));

    }
}
