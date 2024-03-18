package io.hhplus.tdd.userPoint.insert;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.UserPointService;
import org.assertj.core.api.Assertions;
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
public class UserPointInsertTest {

    @Autowired
    UserPointTable userPointTable;

    @Autowired
    UserPointService userPointService;

    @DisplayName("유효한 ID와 충전 금액을 전달했을 때 포인트가 정상적으로 충전되어야 한다.")
    @Test
    void insertUserPointTest() throws InterruptedException {
        UserPoint userPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        UserPoint insertedUserPoint = userPointService.insertUserPoint(userPoint.id(), userPoint.point());
        Assertions.assertThat(insertedUserPoint.point()).isEqualTo(userPoint.point());
    }

    @DisplayName("충전 후, 사용자의 포인트 잔액이 올바르게 업데이트되어야 한다.")
    @Test
    void getUserPointTest() throws InterruptedException {
        UserPoint userPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        UserPoint insertedUserPoint = userPointService.insertUserPoint(userPoint.id(), userPoint.point());
        UserPoint selectedUserPoint = userPointService.selectById(insertedUserPoint.id());
        Assertions.assertThat(selectedUserPoint.point()).isEqualTo(insertedUserPoint.point());
    }

    @DisplayName("ID 또는 포인트가 null인 경우, 충전이 실패해야 한다.")
    @Test
    void validateInsertUserPointNullArguments() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.insertUserPoint(null, null);
                });
    }

    @DisplayName("충전할 포인트가 0 이하일 경우, 충전이 실패해야 한다.")
    @Test
    void validateInsertUserPointNegativePointArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.insertUserPoint(1L, -10L);
                });
    }
}
