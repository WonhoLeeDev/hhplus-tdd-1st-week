package io.hhplus.tdd.database;

import io.hhplus.tdd.point.UserPoint;

public interface UserPointDB {

    public UserPoint selectById(Long id);

    public UserPoint insertOrUpdate(long id, long amount);

}
