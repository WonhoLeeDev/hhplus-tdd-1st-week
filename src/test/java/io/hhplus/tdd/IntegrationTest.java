package io.hhplus.tdd;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointDB;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointController;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.PointHistoryService;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.UserPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    PointController pointController;

    @Autowired
    PointService pointService;

    @Autowired
    UserPointService userPointService;

    @Autowired
    PointHistoryService pointHistoryService;

    @Autowired
    UserPointDB userPointDB;

    @Autowired
    PointHistoryTable pointHistoryTable;

    @BeforeEach
    void setUp() {
        pointHistoryTable = new PointHistoryTable();
        userPointDB = new UserPointTable();
        pointHistoryService = new PointHistoryService(pointHistoryTable);
        userPointService = new UserPointService(userPointDB);
        pointService = new PointService(userPointService, pointHistoryService);
        pointController = new PointController(pointService);
    }

    @Test
    void point_Success() {
        Long id = 1L;
        UserPoint controllerUserPoint = pointController.point(id);
        UserPoint dbUserPoint = userPointDB.selectById(id);

        assertThat(controllerUserPoint.id()).isEqualTo(dbUserPoint.id());
    }

    @Test
    void history_Success() {
        long id = 1L;
        pointController.charge(id, 100L);
        pointController.charge(id, 200L);
        pointController.charge(id, 300L);
        List<PointHistory> controllerHistories = pointController.history(id);
        List<PointHistory> dbPointHistories = pointHistoryTable.selectAllByUserId(id);

        assertThat(controllerHistories.size()).isEqualTo(dbPointHistories.size());
    }

    @Test
    void charge_Success() {
        long id = 1L;
        pointController.charge(id, 100L);
        pointController.charge(id, 200L);
        pointController.charge(id, 300L);
        UserPoint dbUserPoint = userPointDB.selectById(id);

        assertThat(dbUserPoint.point()).isEqualTo(600L);
    }

    @Test
    void use_Success() {
        long id = 1L;
        pointController.charge(id, 100L);
        pointController.charge(id, 200L);
        pointController.charge(id, 300L);
        pointController.use(id, 500L);
        UserPoint dbUserPoint = userPointDB.selectById(id);

        assertThat(dbUserPoint.point()).isEqualTo(100L);
    }
}
