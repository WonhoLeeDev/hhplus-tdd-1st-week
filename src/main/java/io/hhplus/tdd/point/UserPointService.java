package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointTable userPointTable;

    public UserPoint chargeUserPoint(Long id, Long amount) {
        if (id == null || amount == null) {
            throw new IllegalArgumentException("ID 또는 포인트가 null 입니다.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 0 이상이어야 합니다.");
        }
        if(userPointTable.selectById(id).point() > 0L) {
            amount += userPointTable.selectById(id).point();
        }
        return userPointTable.insertOrUpdate(id, amount);
    }

    public UserPoint selectById(Long id){
        return userPointTable.selectById(id);
    }

    public UserPoint useUserPoint(Long id, Long amount) {
        if (id == null || amount == null) {
            throw new IllegalArgumentException("ID 또는 포인트가 null 입니다.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("사용할 포인트는 1 이상이어야 합니다.");
        }
        if (userPointTable.selectById(id).point() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        if (userPointTable.selectById(id).point() >= amount) {
            amount = userPointTable.selectById(id).point() - amount;
        }
        return userPointTable.insertOrUpdate(id, amount);
    }
}
