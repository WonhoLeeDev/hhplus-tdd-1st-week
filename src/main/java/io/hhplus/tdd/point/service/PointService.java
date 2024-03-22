package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;

    public UserPoint point(Long id) {
        return userPointService.getUserPointById(id);
    }

    public List<PointHistory> history(long userId) {
        return pointHistoryService.getPointHistoriesByUserId(userId);
    }

    public UserPoint charge(UserPoint userPoint) {
        UserPoint chargedUserPoint = userPointService.chargeUserPoint(userPoint.id(), userPoint.point());
        pointHistoryService.savePointHistory(chargedUserPoint.id(), chargedUserPoint.point(), TransactionType.CHARGE, chargedUserPoint.updateMillis());
        return chargedUserPoint;
    }

    public UserPoint use(UserPoint userPoint) {
        UserPoint usedUserPoint = userPointService.useUserPoint(userPoint.id(), userPoint.point());
        pointHistoryService.savePointHistory(usedUserPoint.id(), usedUserPoint.point(), TransactionType.USE, usedUserPoint.updateMillis());
        return usedUserPoint;
    }
}
