package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.UserPointDB;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointDB userPointDB;
    private final ConcurrentHashMap<Long, UserPoint> map = new ConcurrentHashMap<>();

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
        return map.compute(id, (userId, userPoint) -> {
            // map에 없는 경우, DB에서 최신 상태를 조회
            if (userPoint == null) {
                userPoint = userPointDB.selectById(id);
            }
            long newAmount = getAmountResult(userPoint.id(), amount, TransactionType.CHARGE);
            return userPointDB.insertOrUpdate(id, newAmount);
        });
    }

    public UserPoint useUserPoint(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용할 포인트는 1 이상이어야 합니다.");
        }
        return map.compute(id, (userId, userPoint) -> {
            // map에 없는 경우, DB에서 최신 상태를 조회
            if (userPoint == null) {
                userPoint = userPointDB.selectById(id);
            }
            if (userPoint.point() < amount) {
                throw new IllegalArgumentException("잔액이 부족합니다.");
            }
            long newAmount = getAmountResult(userPoint.id(), amount, TransactionType.USE);
            return userPointDB.insertOrUpdate(id, newAmount);
        });
    }
    private long getAmountResult(long id, long amount, TransactionType transactionType) {
        long userPoints = getUserPointById(id).point();
        return transactionType == TransactionType.CHARGE ? userPoints + amount : userPoints - amount;
    }
}
