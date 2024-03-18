package io.hhplus.tdd.pointHistory;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * TO-DO:
 * 1. 포인트를 충전했을 때 PointHistoryTable에 포인트 충전내역이 저장되어야 한다.
 * 2. 포인트를 충전했을 때 PointHistoryTable에 포인트 충전금이 누적되어야 한다.
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
    void insertPointHistoryTest() throws InterruptedException {
        UserPoint userPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        PointHistory history = pointHistoryService.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, userPoint.updateMillis());
        List<PointHistory> pointHistories = pointHistoryService.selectAllByUserId(history.userId());
        assertThat(pointHistories.size()).isEqualTo(1);
    }

    @DisplayName("포인트를 충전했을 때 PointHistoryTable에 포인트 충전금이 누적되어야 한다.")
    @Test
    void insertPointHistorySumTest() throws InterruptedException {
        Long userId = 1L;
        Long insertPoint1 = 10L;
        Long insertPoint2 = 20L;
        pointHistoryService.insert(userId, insertPoint1, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryService.insert(userId, insertPoint2, TransactionType.CHARGE, System.currentTimeMillis());

        List<PointHistory> pointHistories = pointHistoryService.selectAllByUserId(userId);

        Long sum = pointHistories.stream()
                .mapToLong(PointHistory::amount)
                .sum();
        assertThat(sum).isEqualTo(insertPoint1 + insertPoint2);
    }
}
