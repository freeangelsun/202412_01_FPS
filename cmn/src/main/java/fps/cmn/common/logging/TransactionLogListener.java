package fps.cmn.common.logging;

import fps.cmn.service.common.logging.TransactionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 트랜잭션 로그 이벤트 리스너 클래스.
 * - 이벤트를 수신하여 트랜잭션 로그를 DB에 저장.
 */
@Component
@RequiredArgsConstructor
public class TransactionLogListener {

    private final TransactionLogService logService;

    /**
     * 트랜잭션 로그 이벤트를 처리하여 DB에 저장.
     * TransactionLogService 호출 중 AOP 순환 호출 방지.
     *
     * @param event 트랜잭션 로그 이벤트
     */
    @EventListener
    public void handleTransactionLogEvent(TransactionLogEvent event) {
        try {
            logService.saveTransactionLog(
                    event.getTransactionId(),
                    event.getModuleId(),
                    event.getMenuId(),
                    event.getUri(),
                    event.getParameters(),
                    event.getResponse(),
                    event.getResponseCode(),
                    event.getErrorMessage(),
                    event.getExecUser(),
                    event.getStartTime(),
                    event.getEndTime(),
                    event.getLogType()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
