package io.hhplus.tdd.step1.userPoint.use;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.UserPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * TO-DO:
 * 1. 유효한 ID와 사용 금액을 전달했을 때, 충분한 잔액이 있으면 포인트가 정상적으로 사용되어야 한다.
 * 2. 포인트가 정상적으로 사용되었으면, 잔액이 업데이트 되어야 한다.
 * 3. ID 또는 사용 포인트가 null일 때, 에러가 던져지고 포인트 사용에 실패한다.
 * 4. 사용할 포인트가 0보다 작으면, 에러가 던져지고 포인트 사용에 실패한다.
 * 5. 잔액 포인트 보다 사용할 포인트가 더 많으면, 에러가 던져지고 포인트 사용에 실패한다.
 */
@SpringBootTest
public class UserPointUseTest {

    @Autowired
    UserPointService userPointService;

    @BeforeEach
    void setUp() {
        userPointService = new UserPointService(new UserPointTable());
    }

    @DisplayName("유효한 ID와 사용 금액을 전달했을 때, 충분한 잔액이 있으면 포인트가 정상적으로 사용 되어야 한다.")
    @Test
    void usePointTest() {
        UserPoint userPoint = new UserPoint(1L, 50L, System.currentTimeMillis());
        assertDoesNotThrow(() -> {
            userPointService.chargeUserPoint(userPoint.id(), userPoint.point());
        });
    }

    @DisplayName("포인트가 정상적으로 사용되었으면, 잔액이 업데이트 되어야 한다.")
    @Test
    void usePointUpdateTest() {
        Long chargePoint = 50L;
        UserPoint userPoint = new UserPoint(1L, chargePoint, System.currentTimeMillis());
        UserPoint chargedUserPoint = userPointService.chargeUserPoint(userPoint.id(), userPoint.point());

        Long usePoint = 30L;
        UserPoint usedUserPoint = userPointService.useUserPoint(chargedUserPoint.id(), usePoint);
        assertThat(usedUserPoint.point()).isEqualTo(chargePoint - usePoint);
    }

//    @DisplayName("ID 또는 사용 포인트가 null일 때, 에러가 던져지고 포인트 사용에 실패한다.")
//    @Test
//    void validateusePointNullArgument() {
//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(() -> {
//                    userPointService.usePoint(null, null);
//                }).withMessage("ID 또는 포인트가 null 입니다.");
//    }

    @DisplayName("사용할 포인트가 0보다 작으면, 에러가 던져지고 포인트 사용에 실패한다.")
    @Test
    void validateusePointNegativePointArgument() {
        UserPoint userPoint = new UserPoint(1L, 50L, System.currentTimeMillis());
        UserPoint chargedUserPoint = userPointService.chargeUserPoint(userPoint.id(), userPoint.point());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(chargedUserPoint.id(), -50L);
                }).withMessage("사용할 포인트는 1 이상이어야 합니다.");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(chargedUserPoint.id(), 0L);
                }).withMessage("사용할 포인트는 1 이상이어야 합니다.");
    }

    @DisplayName("잔액 포인트 보다 사용할 포인트가 더 많으면, 에러가 던져지고 포인트 사용에 실패한다.")
    @Test
    void validateusePointAmountArgument() {
        UserPoint userPoint = new UserPoint(1L, 50L, System.currentTimeMillis());
        UserPoint chargedUserPoint = userPointService.chargeUserPoint(userPoint.id(), userPoint.point());

        Long usePoint = 51L;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(chargedUserPoint.id(), usePoint);
                }).withMessage("잔액이 부족합니다.");
    }

}
