package io.hhplus.tdd.step2;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.service.PointHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PointHistoryServiceMockTest {

    @Mock
    PointHistoryTable pointHistoryTable;

    @InjectMocks
    PointHistoryService pointHistoryService;


    @DisplayName("인자값이 모두 유효하면, PointHistory 데이터 저장에 성공한다.")
    @Test
    void savePointHistory_Success_ifWithValidArguments() {
        // given
        long userPointId = 1L;
        long amount = 10L;
        TransactionType type = TransactionType.CHARGE;
        long updateMillis = System.currentTimeMillis();
        PointHistory expectedPointHistory = new PointHistory(1L, userPointId, amount, type, updateMillis);

        // when
        when(pointHistoryTable.insert(userPointId, amount, type, updateMillis)).thenReturn(expectedPointHistory);
        PointHistory result = pointHistoryService.savePointHistory(userPointId, amount, type, updateMillis);

        // then
        assertThat(result).isEqualTo(expectedPointHistory);
        verify(pointHistoryTable).insert(userPointId, amount, type, updateMillis);
    }

//    @Test
//    void savePointHistory_Success_ifWithValidArguments2() {
//        // given
//        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
//        PointHistoryService pointHistoryService = new PointHistoryService(mockPointHistoryTable);
//
//        long userPointId = 1L;
//        long amount = 10L;
//        TransactionType type = TransactionType.CHARGE;
//        long updateMillis = System.currentTimeMillis();
//
//        // when
//        pointHistoryService.savePointHistory(userPointId, amount, type, updateMillis);
//
//        // then
//        verify(mockPointHistoryTable).insert(userPointId, amount, type, updateMillis);
//    }

    @DisplayName("포인트가 0이하면, IllegalArgumentException 예외를 던지고 저장에 실패한다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void savePointHistory_ThrowsException_ifPointInvalid(long amount) {
        // given
        long userPointId = 1L;
        TransactionType type = TransactionType.CHARGE;
        long updateMillis = System.currentTimeMillis();

        // when&then
        assertThrows(IllegalArgumentException.class, () -> {
            pointHistoryService.savePointHistory(userPointId, amount, type, updateMillis);
        });
    }

    @DisplayName("TransactionType이 null이면, IllegalArgumentException 예외를 던지고 저장에 실패한다.")
    @Test
    void savePointHistory_ThrowsException_ifTransactionTypeNull() {
        // given
        TransactionType type = null;

        // when&then
        assertThrows(IllegalArgumentException.class, () -> {
            pointHistoryService.savePointHistory(1L, 10L, type, System.currentTimeMillis());
        });
    }

    @DisplayName("PointHistory 조회에 성공한다.")
    @Test
    void getPointHistoriesByUserId_Success() {
        long userPointId = 1L;
        // given
        long updateMillis = System.currentTimeMillis();
        List<PointHistory> expectedPointHistories = Arrays.asList(
                new PointHistory(1L, userPointId, 10L, TransactionType.CHARGE, updateMillis),
                new PointHistory(2L, userPointId, 20L, TransactionType.CHARGE, updateMillis + 300L)
        );
        // when
        when(pointHistoryTable.selectAllByUserId(userPointId)).thenReturn(expectedPointHistories);
        List<PointHistory> result = pointHistoryService.getPointHistoriesByUserId(userPointId);

        // then
        assertThat(result).isEqualTo(expectedPointHistories);
        verify(pointHistoryTable).selectAllByUserId(userPointId);
    }
}
