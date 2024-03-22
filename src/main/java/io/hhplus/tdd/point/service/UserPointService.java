package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.UserPointDB;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointDB userPointDB;

    public UserPoint getUserPointById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 값이 null입니다.");
        }
        return userPointDB.selectById(id);
    }

    public UserPoint chargeUserPoint(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 1 이상이어야 합니다.");
        }
        amount = getAmountResult(id, amount, TransactionType.CHARGE);
        return userPointDB.insertOrUpdate(id, amount);
    }

    public UserPoint useUserPoint(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용할 포인트는 1 이상이어야 합니다.");
        }
        if (userPointDB.selectById(id).point() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        amount = getAmountResult(id, amount, TransactionType.USE);
        return userPointDB.insertOrUpdate(id, amount);
    }

    private long getAmountResult(long id, long amount, TransactionType transactionType) {
        long userPoints = getUserPointById(id).point();
        return transactionType == TransactionType.CHARGE ? userPoints + amount : userPoints - amount;
    }
}
