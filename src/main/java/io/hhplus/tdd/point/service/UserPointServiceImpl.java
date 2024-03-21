package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointServiceImpl implements UserPointService {

    private final UserPointTable userPointTable;

    @Override
    public UserPoint getUserPointById(Long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public UserPoint chargeUserPoint(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 0 이상이어야 합니다.");
        }
        amount = getAmountResult(id, amount, TransactionType.CHARGE);
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public UserPoint useUserPoint(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용할 포인트는 1 이상이어야 합니다.");
        }
        if (userPointTable.selectById(id).point() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        amount = getAmountResult(id, amount, TransactionType.USE);
        return userPointTable.insertOrUpdate(id, amount);
    }

    private long getAmountResult(long id, long amount, TransactionType transactionType) {
        long userPoints = getUserPointById(id).point();
        return transactionType == TransactionType.CHARGE ? userPoints + amount : userPoints - amount;
    }
}
