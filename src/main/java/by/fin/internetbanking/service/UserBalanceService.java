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
}
