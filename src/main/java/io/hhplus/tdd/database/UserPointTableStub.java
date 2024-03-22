package io.hhplus.tdd.database;

import io.hhplus.tdd.point.UserPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * 해당 Table 클래스는 테스트용 Stub 클래스입니다.
 */
public class UserPointTableStub implements UserPointDB{
    private final Map<Long, UserPoint> table = new HashMap<>();

    public UserPointTableStub(long defaultId, long defaultPoint) {
        table.put(defaultId, new UserPoint(defaultId, defaultPoint, System.currentTimeMillis()));
    }

    @Override
    public UserPoint selectById(Long id) {
        return table.getOrDefault(id, UserPoint.empty(id));
    }

    @Override
    public UserPoint insertOrUpdate(long id, long amount) {
        UserPoint userPoint = new UserPoint(id, amount, System.currentTimeMillis());
        table.put(id, userPoint);
        return userPoint;
    }
}
