package io.hhplus.tdd.step2;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.UserPointService;
import io.hhplus.tdd.point.service.UserPointServiceStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserPointServiceStubTest {

    UserPointService userPointService;

    @BeforeEach
    void setUp() {
        userPointService = new UserPointServiceStub();
    }

    @DisplayName("id로 UserPoint를 조회하면, 해당 id의 UserPoint 조회에 성공한다.")
    @Test
    void getUserPointById_Success() {
        Long userPointId = 1L;
        UserPoint userPoint = userPointService.getUserPointById(userPointId);

        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(userPointId);
        assertThat(userPoint.point()).isEqualTo(10L);
    }

    @DisplayName("id를 null로 조회하면, Exception을 던지고 조회에 실패한다.")
    @Test
    void getUserPointById_ThrowsException_ifNullId() {
        Long userPointId = null;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.getUserPointById(userPointId);
                });
    }

    @DisplayName("id와 amount가 유효하면, 충전에 성공한다.")
    @Test
    void chargeUserPoint_Success() {
        long userPointId = 1L;
        long amount = 50L;
        UserPoint userPoint = userPointService.chargeUserPoint(userPointId, amount);
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(userPointId);
        assertThat(userPoint.point()).isEqualTo(50L);
    }

    @DisplayName("충전할 amount가 0 이하면, Exception을 던지고 충전에 실패한다.")
    @Test
    void chargeUserPoint_ThrowsException_ifAmountLessThan1L() {
        long userPointId = 1L;

        long zeroAmount = 0L;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.chargeUserPoint(userPointId, zeroAmount);
                });

        long negativeAmount = -50L;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.chargeUserPoint(userPointId, negativeAmount);
                });
    }


    @DisplayName("id와 amount가 유효하면, 충전에 성공한다.")
    @Test
    void useUserPoint_Success() {
        long userPointId = 1L;
        long amount = 50L;
        UserPoint userPoint = userPointService.useUserPoint(userPointId, amount);
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(userPointId);
        assertThat(userPoint.point()).isEqualTo(90L);
    }

    @DisplayName("사용할 amount가 0 이하면, Exception을 던지고 충전에 실패한다.")
    @Test
    void useUserPoint_ThrowsException_ifAmountLessThan1L() {
        long userPointId = 1L;

        long zeroAmount = 0L;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(userPointId, zeroAmount);
                });

        long negativeAmount = -50L;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(userPointId, negativeAmount);
                });
    }

    @DisplayName("id와 amount가 유효하면, 충전에 성공한다.")
    @Test
    void useUserPoint_ThrowsException_ifUsePointMoreThanHave() {
        long userPointId = 1L;
        long amount = 100L;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(userPointId, amount);
                });
    }
}
