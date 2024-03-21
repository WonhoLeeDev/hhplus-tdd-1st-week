package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointServiceImpl userPointServiceImpl;
    private final PointHistoryService pointHistoryService;

    public UserPoint charge(UserPoint userPoint) {
        UserPoint chargedUserPoint = userPointServiceImpl.chargeUserPoint(userPoint.id(), userPoint.point());
        pointHistoryService.savePointHistory(chargedUserPoint.id(), chargedUserPoint.point(), TransactionType.CHARGE, chargedUserPoint.updateMillis());
        return chargedUserPoint;
    }


    public UserPoint use(UserPoint userPoint) {
        UserPoint usedUserPoint = userPointServiceImpl.useUserPoint(userPoint.id(), userPoint.point());
        pointHistoryService.savePointHistory(usedUserPoint.id(), usedUserPoint.point(), TransactionType.USE, usedUserPoint.updateMillis());
        return usedUserPoint;
    }
}
