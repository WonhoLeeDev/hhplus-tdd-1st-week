package io.hhplus.tdd.step2;

import io.hhplus.tdd.database.UserPointDB;
import io.hhplus.tdd.database.UserPointTableStub;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.UserPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserPointServiceStubTest {

    UserPointService userPointService;
    UserPointDB userPointDB;

    @BeforeEach
    void setUp() {
        userPointDB = new UserPointTableStub(3L, 100L);
        userPointService = new UserPointService(userPointDB);
    }

    @DisplayName("id로 UserPoint를 조회하면, 해당 id의 UserPoint 조회에 성공한다.")
    @Test
    void getUserPointById_Success() {
        Long userPointId = 3L;

        UserPoint resultUserPoint = userPointService.getUserPointById(userPointId);

        assertThat(resultUserPoint.id()).isEqualTo(3L);
        assertThat(resultUserPoint.point()).isEqualTo(100L);
    }

    @DisplayName("id를 null로 조회하면, 예외를 던지고 포인트 조회에 실패한다.")
    @Test
    void getUserPointById_ThrowsException_ifIdNull() {
        Long userPointId = null;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.getUserPointById(userPointId);
                });
    }

    @DisplayName("id와 포인트 amount가 유효하면, 포인트 충전에 성공한다.")
    @Test
    void chargeUserPoint_Success_ifWithValidArguments() {
        long userPointId = 1L;
        long amount = 50L;
        userPointService.chargeUserPoint(userPointId, amount);

        UserPoint result = userPointDB.insertOrUpdate(userPointId, amount);

        assertThat(1L).isEqualTo(result.id());
        assertThat(50L).isEqualTo(result.point());
    }

    @DisplayName("충전할 포인트 amount가 0 이하면, 예외를 던지고 포인트 충전에 실패한다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void chargeUserPoint_ThrowsException_ifAmountLessThan1L(long amount) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.chargeUserPoint(1L, amount);
                });
    }

    @DisplayName("id와 포인트 amount가 유효하면, 포인트 사용에 성공한다.")
    @Test
    void useUserPoint_Success_ifWithValidArguments() {
        long userPointId = 3L;
        long amount = 30L;
        userPointService.useUserPoint(userPointId, amount);

        UserPoint userPoint = userPointDB.selectById(userPointId);

        assertThat(3L).isEqualTo(userPoint.id());
        assertThat(70L).isEqualTo(userPoint.point());
    }

    @DisplayName("사용할 포인트 amount가 0 이하면, 예외를 던지고 포인트 사용에 실패한다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void useUserPoint_ThrowsException_ifAmountLessThan1L(long amount) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(1L, amount);
                });
    }

    @DisplayName("사용할 포인트 amount가 보유한 포인트 보다 크면, 예외를 던지고 포인트 사용에 실패한다.")
    @Test
    void useUserPoint_ThrowsException_ifUsePointMoreThanHave() {
        long userPointId = 3L;
        long amount = 150L;

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    userPointService.useUserPoint(userPointId, amount);
                });
    }
}
