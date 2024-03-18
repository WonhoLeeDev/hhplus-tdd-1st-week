package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointTable userPointTable;

    public UserPoint insertUserPoint(Long id, Long amount) throws InterruptedException {
        if (id == null || amount == null) {
            throw new IllegalArgumentException("ID 또는 포인트가 null 입니다.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 0 이상이어야 합니다.");
        }

        return userPointTable.insertOrUpdate(id, amount);
    }

    public UserPoint selectById(Long id) throws InterruptedException {
        return userPointTable.selectById(id);
    }
}
