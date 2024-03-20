package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;

    public UserPoint charge(UserPoint userPoint) {
        UserPoint chargedUserPoint = userPointService.chargePoint(userPoint.id(), userPoint.point());
        pointHistoryService.savePointHistory(chargedUserPoint.id(), chargedUserPoint.point(), TransactionType.CHARGE, chargedUserPoint.updateMillis());
        return chargedUserPoint;
    }


    public UserPoint use(UserPoint userPoint) {
        UserPoint usedUserPoint = userPointService.usePoint(userPoint.id(), userPoint.point());
        pointHistoryService.savePointHistory(usedUserPoint.id(), usedUserPoint.point(), TransactionType.USE, usedUserPoint.updateMillis());
        return usedUserPoint;
    }
}
