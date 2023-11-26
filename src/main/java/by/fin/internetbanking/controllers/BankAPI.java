package by.fin.internetbanking.controllers;

import by.fin.internetbanking.dtos.UserOpsAnswer;
import by.fin.internetbanking.entity.UserOperation;
import by.fin.internetbanking.service.UserBalanceService;
import by.fin.internetbanking.service.UserOperationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@RestController()
@RequestMapping("bank-api-v1.0")
public class BankAPI {

    private final UserBalanceService userBalanceService;
    private final UserOperationService userOperationService;

    public BankAPI(UserBalanceService userBalanceService, UserOperationService userOperationService) {
        this.userBalanceService = userBalanceService;
        this.userOperationService = userOperationService;
    }

    @GetMapping("/getOperationList")
    @Operation(summary = "add some money on banking account")
    public ResponseEntity<List<UserOpsAnswer>> getOperationList(
            @RequestParam String userId,
            @RequestParam(required = false) ZonedDateTime beginDateTime,
            @RequestParam(required = false) ZonedDateTime endDateTime
    ) {
        List<UserOpsAnswer> opsList = userOperationService.getOperations(userId,beginDateTime,endDateTime);
        return ResponseEntity.ok(opsList);
    }

    @GetMapping("/getBalance/{userId}")
    @Operation(summary = "Get balance by userId or -1 with an error message explaining the problem.")
    public ResponseEntity<CustomResponse> getBalance(@PathVariable(name = "userId") String userId)
    {
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
    @Operation(summary = "Withdraw some money from banking account")
    @Transactional
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

                operationResult = userOperationService.buildAndSave(longUserId, UserOperation.OperationType.TAKE, moneyAmount);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(1, "Successful withdraw for user " + longUserId + " of " + moneyAmount + "$"));
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(0, "User ("+ userId + " don't have enough money (" + moneyAmount + ")"));
            }

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new CustomResponse(-1, "Invalid UserID format"));
        }
    }

    @GetMapping("/putMoney")
    @Operation(summary = "add some money on banking account")
    @Transactional
    public ResponseEntity<CustomResponse> putMoney(
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

            int operationResult = userBalanceService.addMoney(longUserId, moneyAmount);
            if (operationResult == 1) {
                operationResult = userOperationService.buildAndSave(longUserId, UserOperation.OperationType.ADD, moneyAmount);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(1, "Successful added " + moneyAmount + " for user " + longUserId + " and new balance  " + userBalanceService.getBalance(longUserId) + "$"));
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(0, "something went wrong and " + moneyAmount + " wasn't added to balance"));
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
