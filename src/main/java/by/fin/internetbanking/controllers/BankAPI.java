package by.fin.internetbanking.controllers;

import by.fin.internetbanking.service.UserBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController()
@RequestMapping("bank-api-v1.0")
public class BankAPI {

    private final UserBalanceService userBalanceService;

    public BankAPI(UserBalanceService userBalanceService) {
        this.userBalanceService = userBalanceService;
    }


    @GetMapping("/getBalance/{userId}")
    @Operation(summary = "Get balance by userId or -1 with an error message explaining the problem.")
    public ResponseEntity<CustomResponse> getBalance(@PathVariable(name = "userId") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body(new CustomResponse(-1, "UserID is required"));
        }

        try {
            Long longUserId = Long.valueOf(userId);
//            if (userBalances.containsKey(longUserId)) {
            if (userBalanceService.doesUserExist(longUserId)) {
                BigDecimal balance = userBalanceService.getBalance(longUserId);
                return ResponseEntity.ok(new CustomResponse(balance, "It's your balance"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(-1, "User with UserID " + longUserId + " not found."));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new CustomResponse(-1, "Invalid UserID format"));
        }
    }

    @GetMapping("/takeMoney")
    @Operation(summary = "Get balance by userId or -1 with an error message explaining the problem.")
    public ResponseEntity<CustomResponse> takeMoney(
            @RequestParam String userId,
            @RequestParam BigDecimal moneyAmount
    ) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body(new CustomResponse(-1, "UserID is required"));
        }
        if (moneyAmount == null) {
            return ResponseEntity.badRequest().body(new CustomResponse(-1, "moneyAmount is required"));
        }

        try {
            Long longUserId = Long.valueOf(userId);
            if (!userBalanceService.doesUserExist(longUserId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(-1, "User with UserID " + longUserId + " not found."));
            }
            int operationResult = userBalanceService.withdrawMoney(longUserId, moneyAmount);
            if (operationResult == 1) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(1, "Successful withdraw for user " + longUserId + " of " + moneyAmount + "$"));
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(0, "User ("+ userId + " don't have enough money (" + moneyAmount + ")"));
            }

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new CustomResponse(-1, "Invalid UserID format"));
        }
    }

    @Data
    @AllArgsConstructor
    class CustomResponse {
        BigDecimal code;
        String message;

        public CustomResponse(int code, String message) {
            this.code = new BigDecimal(code);
            this.message = message;
        }
    }

}
