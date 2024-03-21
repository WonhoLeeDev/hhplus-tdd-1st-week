package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;

public class UserPointServiceStub implements UserPointService {
    @Override
    public UserPoint getUserPointById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Exception 발생. 조회할 ID 값이 null입니다.");
        }
        return new UserPoint(1L, 10L, System.currentTimeMillis());
    }

    @Override
    public UserPoint chargeUserPoint(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Exception 발생. 충전액은 0보다 커야합니다.");
        }
        return new UserPoint(1L, 50L, System.currentTimeMillis());
    }

    @Override
    public UserPoint useUserPoint(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Exception 발생. 사용액은 0보다 커야합니다.");
        }
        UserPoint userPointStub = new UserPoint(1L, 90L, System.currentTimeMillis());
        if (amount > userPointStub.point()) {
            throw new IllegalArgumentException("Exception 발생. 보유한 포인트 보다 사용액이 더 많습니다.");
        }
        return userPointStub;
    }
}
