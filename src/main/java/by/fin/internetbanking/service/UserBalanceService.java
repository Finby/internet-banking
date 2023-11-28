package by.fin.internetbanking.service;

import by.fin.internetbanking.entity.UserBalance;
import by.fin.internetbanking.repository.UserBalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class UserBalanceService {


    private final UserBalanceRepository userBalanceRepository;

    public boolean doesUserExist(Long id) {
        return userBalanceRepository.existsById(id);
    }
    public BigDecimal getBalance(Long id) {
        UserBalance userBalance = userBalanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        return userBalance.getBalance();
    }

    public int withdrawMoney(Long longUserId, BigDecimal moneyAmount) {
        UserBalance userBalance = userBalanceRepository.findById(longUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + longUserId));
        int result =  userBalance.removeMoney(moneyAmount);
        if (result == 1) {
            userBalanceRepository.save(userBalance);
        }
        return result;
    }

    public int addMoney(Long longUserId, BigDecimal moneyAmount) {
        UserBalance userBalance = userBalanceRepository.findById(longUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + longUserId));
        int result =  userBalance.addMoney(moneyAmount);
        if (result == 1) {
            userBalanceRepository.save(userBalance);
        }
        return result;
    }

    @Transactional
    public int transferMoneyFromUserToUser(Long toUserId, Long fromUserId, BigDecimal moneyAmount) throws NotEnoughMoneyException {
        UserBalance toUserBalance = userBalanceRepository.findById(toUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + toUserId));
        UserBalance fromUserBalance = userBalanceRepository.findById(fromUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + fromUserId));
        if (moneyAmount.compareTo(fromUserBalance.getBalance()) == 1) {
            throw new NotEnoughMoneyException("Not enough money on balance of user " + fromUserBalance.getId() + " to transfer " + moneyAmount);
        }
        int result1 = fromUserBalance.removeMoney(moneyAmount);
        int result2 = toUserBalance.addMoney(moneyAmount);
        if (result1 == 1 && result2 == 1) {
            userBalanceRepository.save(toUserBalance);
            userBalanceRepository.save(fromUserBalance);
            return 1;
        }

        return 0;
    }
}
