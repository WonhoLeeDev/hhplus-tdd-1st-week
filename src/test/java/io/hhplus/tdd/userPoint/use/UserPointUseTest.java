package io.hhplus.tdd.userPoint.use;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.UserPointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * TO-DO:
 * 1. 유효한 ID와 사용 금액을 전달했을 때, 충분한 잔액이 있으면 포인트가 정상적으로 사용되어야 한다.
 * 2. 사용 후, 사용자의 포인트 잔액이 올바르게 업데이트되어야 한다.
 * 3. 잔액이 부족할 경우, 포인트 사용 요청이 실패해야 한다.
 * 4. 잘못된 ID를 전달했을 경우, 사용이 실패해야 한다.
 * 5. 음수 금액을 사용하려 할 경우, 사용이 실패해야 한다.
 * 6. 동시에 여러 건의 사용 요청이 처리될 수 있어야 하며, 올바른 순서와 결과를 보장해야 한다.
 */
@SpringBootTest
public class UserPointUseTest {

}
