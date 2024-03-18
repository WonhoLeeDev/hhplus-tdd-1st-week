package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryTable pointHistoryTable;

    public PointHistory insert(Long userId, Long point, TransactionType transactionType, Long updateMillis) throws InterruptedException {
        return pointHistoryTable.insert(userId, point, transactionType, updateMillis);
    }

    public List<PointHistory> selectAllByUserId(Long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
