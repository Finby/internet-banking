package by.fin.internetbanking.service;

import by.fin.internetbanking.entity.UserBalance;
import by.fin.internetbanking.repository.UserBalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
//        if (result == 1) {
//            UserBalance userBalance2 = userBalanceRepository.save(userBalance);
//        }
        return userBalance.removeMoney(moneyAmount);
    }
}
