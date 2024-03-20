package io.hhplus.tdd.firstWeek.pointHistory;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * TO-DO:
 * 1. 포인트를 충전했을 때 PointHistoryTable에 포인트 충전내역이 저장되어야 한다.
 * 2. TransactionType과 updateMillis의 값이 유효하지 않으면, 충전내역 저장에 실패 한다.
 * 3. 포인트를 충전했을 때 PointHistoryTable에 포인트 충전금이 누적되어야 한다.
 */
@SpringBootTest
public class PointHistoryTest {

    @Autowired
    PointHistoryService pointHistoryService;

    @BeforeEach
    void setup() {
        pointHistoryService = new PointHistoryService(new PointHistoryTable());
    }

    @DisplayName("포인트를 충전했을 때 PointHistoryTable에 포인트 충전내역이 저장되어야 한다.")
    @Test
    void insertPointHistoryTest() {
        UserPoint userPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        assertDoesNotThrow(() -> {
            pointHistoryService.savePointHistory(userPoint.id(), userPoint.point(), TransactionType.CHARGE, userPoint.updateMillis());
        });
    }

    @DisplayName("TransactionType과 updateMillis의 값이 유효하지 않으면, 충전내역 저장에 실패 한다.")
    @Test
    void validateInsertPointHistoryArgumentsTest() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    pointHistoryService.savePointHistory(1L, 10L, null, System.currentTimeMillis());
                }).withMessage("TransactionType 값이 null 입니다.");

//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(() -> {
//                    pointHistoryService.insert(1L, 10L, TransactionType.CHARGE, null);
//                }).withMessage("updateMillis 값이 null 입니다.");
    }

    @DisplayName("포인트를 충전했을 때 PointHistoryTable에 포인트 충전금이 누적되어야 한다.")
    @Test
    void insertPointHistorySumTest() {
        Long userId = 1L;
        Long insertPoint1 = 10L;
        Long insertPoint2 = 20L;
        pointHistoryService.savePointHistory(userId, insertPoint1, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryService.savePointHistory(userId, insertPoint2, TransactionType.CHARGE, System.currentTimeMillis());

        List<PointHistory> pointHistories = pointHistoryService.getPointHistoriesByUserId(userId);

        Long sum = pointHistories.stream()
                .mapToLong(PointHistory::amount)
                .sum();
        assertThat(sum).isEqualTo(insertPoint1 + insertPoint2);
    }
}
