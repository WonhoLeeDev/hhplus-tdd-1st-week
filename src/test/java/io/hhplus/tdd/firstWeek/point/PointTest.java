package io.hhplus.tdd.firstWeek.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistoryService;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.UserPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class PointTest {

    @Autowired
    PointService pointService;

    @BeforeEach
    void setUp() {
        pointService = new PointService(
                new UserPointService(new UserPointTable()),
                new PointHistoryService(new PointHistoryTable())
        );
    }

    @DisplayName("포인트 충전 시, 해당내역이 UserPoint와 PointHistory에 예외발생 없이 정상적으로 저장된다.")
    @Test
    void chargePointTest() {
        UserPoint userPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        assertDoesNotThrow(() -> {
            pointService.charge(userPoint);
        });
    }

    @DisplayName("포인트 사용 시, UserPoint와 PointHistory에 예외발생 없이 정상적으로 저장된다.")
    @Test
    void usePointTest() {
        UserPoint chargeUserPoint = new UserPoint(1L, 20L, System.currentTimeMillis());
        pointService.charge(chargeUserPoint);

        UserPoint useUserPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        assertDoesNotThrow(() -> {
            pointService.use(useUserPoint);
        });
    }


}
