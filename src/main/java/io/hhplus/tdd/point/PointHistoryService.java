package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryTable pointHistoryTable;

    public PointHistory savePointHistory(long userId, long point, TransactionType transactionType, long updateMillis) {
        if (transactionType == null) {
            throw new IllegalArgumentException("TransactionType 값이 null 입니다.");
        }
        return pointHistoryTable.insert(userId, point, transactionType, updateMillis);
    }

    public List<PointHistory> getPointHistoriesByUserId(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
