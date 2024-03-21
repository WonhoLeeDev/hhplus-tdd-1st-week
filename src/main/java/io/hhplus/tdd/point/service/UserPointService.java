package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;

public interface UserPointService {

    public UserPoint getUserPointById(Long id);

    public UserPoint chargeUserPoint(long id, long amount);

    public UserPoint useUserPoint(long id, long amount);
}
