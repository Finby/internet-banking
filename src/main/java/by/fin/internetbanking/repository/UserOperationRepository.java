package by.fin.internetbanking.repository;

import by.fin.internetbanking.dtos.UserOpsAnswer;
import by.fin.internetbanking.entity.UserOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface UserOperationRepository extends JpaRepository<UserOperation, Long> {

    @Query("SELECT new by.fin.internetbanking.dtos.UserOpsAnswer(u.createdDate, u.operation, u.amountOfMoney) FROM UserOperation u WHERE u.userId = :userId")
    List<UserOpsAnswer> findSelectedFieldsByUserId(Long userId);

    @Query("SELECT new by.fin.internetbanking.dtos.UserOpsAnswer(u.createdDate, u.operation, u.amountOfMoney) FROM UserOperation u WHERE u.userId = :userId AND u.createdDate BETWEEN :beginCreatedDate AND :endCreatedDate")
    List<UserOpsAnswer> findSelectedFieldsByUserIdAndDateRange(@Param("userId") Long userId, @Param("beginCreatedDate") ZonedDateTime beginCreatedDate, @Param("endCreatedDate") ZonedDateTime endCreatedDate);

    @Query("SELECT new by.fin.internetbanking.dtos.UserOpsAnswer(u.createdDate, u.operation, u.amountOfMoney) FROM UserOperation u WHERE u.userId = :userId AND u.createdDate >= :beginCreatedDate")
    List<UserOpsAnswer> findSelectedFieldsByUserIdAndAfterBeginDate(@Param("userId") Long userId, @Param("beginCreatedDate") ZonedDateTime beginCreatedDate);

    @Query("SELECT new by.fin.internetbanking.dtos.UserOpsAnswer(u.createdDate, u.operation, u.amountOfMoney) FROM UserOperation u WHERE u.userId = :userId AND u.createdDate > :beginCreatedDate AND u.createdDate <= :endCreatedDate")
    List<UserOpsAnswer> findSelectedFieldsByUserIdAndBeforeEndDate(@Param("userId") Long userId, @Param("endCreatedDate") ZonedDateTime endCreatedDate);


}
