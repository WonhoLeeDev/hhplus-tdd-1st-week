package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointHistoryService{

    private final PointHistoryTable pointHistoryTable;

    public PointHistory savePointHistory(long userId, long point, TransactionType transactionType, long updateMillis) {
        validateArgs(point, transactionType);
        return pointHistoryTable.insert(userId, point, transactionType, updateMillis);
    }

    public List<PointHistory> getPointHistoriesByUserId(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
    private static void validateArgs(long point, TransactionType transactionType) {
        if (point <= 0) {
            throw new IllegalArgumentException("point 값이 0보다 커야합니다.");
        }
        if (transactionType == null) {
            throw new IllegalArgumentException("TransactionType 값이 null 입니다.");
        }
    }
}
