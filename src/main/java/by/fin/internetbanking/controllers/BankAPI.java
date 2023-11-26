package by.fin.internetbanking.controllers;

import by.fin.internetbanking.service.UserBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("bank-api-v1.0")
public class BankAPI {

    // Simulated user data for demonstration purposes
    private static final Map<Long, Double> userBalances = new HashMap<>();

    static {
        userBalances.put(1L, 100.0);
        userBalances.put(2L, 50.0);
        userBalances.put(3L, 200.0);
    }



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
