package io.hhplus.tdd.step1.userPoint.charge;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.UserPointServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * TO-DO:
 * 1. 유효한 ID와 충전 금액을 전달했을 때 포인트가 정상적으로 충전되어야 한다.
 * 2. 충전 후, 사용자의 포인트 잔액이 올바르게 업데이트되어야 한다.
 * 3. ID 또는 포인트가 null인 경우, 충전이 실패해야 한다.
 * 4. 음수 금액을 충전하려 할 경우, 충전이 실패해야 한다.
 */
@SpringBootTest
public class UserPointChargeTest {

    @Autowired
    UserPointServiceImpl userPointServiceImpl;

    @BeforeEach
    void setUp() {
        userPointServiceImpl = new UserPointServiceImpl(new UserPointTable());
    }

    @DisplayName("유효한 ID와 충전 금액을 전달했을 때 포인트가 정상적으로 충전되어야 한다.")
    @Test
    void chargePointTest() {
        UserPoint userPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        UserPoint chargedUserPoint = userPointServiceImpl.chargeUserPoint(userPoint.id(), userPoint.point());
        Assertions.assertThat(chargedUserPoint.point()).isEqualTo(userPoint.point());
    }

    @DisplayName("충전 후, 사용자의 포인트 잔액이 누적되어 올바르게 업데이트되어야 한다.")
    @Test
    void updateUserPointTest() {
        Long userId = 1L;
        UserPoint userPoint1 = new UserPoint(userId, 10L, System.currentTimeMillis());
        userPointServiceImpl.chargeUserPoint(userPoint1.id(), userPoint1.point());

        UserPoint userPoint2 = new UserPoint(userId, 20L, System.currentTimeMillis());
        userPointServiceImpl.chargeUserPoint(userPoint2.id(), userPoint2.point());

        UserPoint selectedUserPoint = userPointServiceImpl.getUserPointById(userId);
        Assertions.assertThat(selectedUserPoint.point()).isEqualTo(30L);
    }

//    @DisplayName("ID 또는 포인트가 null인 경우, 충전이 실패해야 한다.")
//    @Test
//    void validateInsertUserPointNullArguments() {
//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(() -> {
//                    userPointService.chargePoint(null, null);
//                });
//    }

    @DisplayName("충전할 포인트가 0 이하일 경우, 충전이 실패해야 한다.")
    @Test
    void validateInsertUserPointNegativePointArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointServiceImpl.chargeUserPoint(1L, -10L);
                });
    }
}
