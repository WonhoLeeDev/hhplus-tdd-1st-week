package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;

    public PointHistory charge(UserPoint userPoint) {
        UserPoint chargedUserPoint = userPointService.chargeUserPoint(userPoint.id(), userPoint.point());
        return pointHistoryService.insert(chargedUserPoint.id(), chargedUserPoint.point(), TransactionType.CHARGE, chargedUserPoint.updateMillis());
    }


    public PointHistory use(UserPoint userPoint) {
        UserPoint usedUserPoint = userPointService.useUserPoint(userPoint.id(), userPoint.point());
        return pointHistoryService.insert(usedUserPoint.id(), usedUserPoint.point(), TransactionType.USE, usedUserPoint.updateMillis());
    }
}
