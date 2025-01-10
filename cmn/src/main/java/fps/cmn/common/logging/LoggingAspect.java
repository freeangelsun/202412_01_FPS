package fps.cmn.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

/**
 * 트랜잭션 로깅을 위한 AOP 클래스.
 * - 컨트롤러 및 서비스 계층의 메서드를 가로채어 실행 전후 로깅 처리
 * - 트랜잭션 성공 및 실패 이벤트를 발행하여 DB에 로그를 저장하도록 처리
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 생성자
     *
     * @param eventPublisher 이벤트 발행기
     */
    public LoggingAspect(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 컨트롤러 및 서비스 계층의 모든 메서드를 가로채어 로깅 처리.
     * TransactionLogService는 순환 호출 방지를 위해 제외.
     *
     * @param joinPoint AOP에서 가로챈 메서드 정보
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 중 발생하는 예외
     */
    @Around("execution(* fps..*Controller.*(..)) "+ //|| execution(* fps..*Service.*(..))" +
            " && !execution(* fps.cmn.service.common.logging.TransactionLogService.*(..))")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionId = generateTransactionId();
        long startTime = Instant.now().toEpochMilli();

        // 메서드 인자 추출
        Object[] args = joinPoint.getArgs();
        String menuId = extractParameter(args, "menuId");
        String execUser = extractParameter(args, "execUser");

        try {
            logger.info("Transaction Started: ID={}, Method={}, Args={}",
                    transactionId, joinPoint.getSignature().toShortString(), Arrays.toString(args));

            // 실제 메서드 실행
            Object result = joinPoint.proceed();
            long endTime = Instant.now().toEpochMilli();

            logger.info("Transaction Completed: ID={}, ExecutionTime={}ms",
                    transactionId, (endTime - startTime));

            // 성공 이벤트 발행
            publishTransactionLog(transactionId, "SUCCESS", "ACC", menuId, joinPoint.getSignature().toShortString(),
                    Arrays.toString(args), result != null ? result.toString() : null, 200, null,
                    execUser, startTime, endTime);
            return result;
        } catch (Exception e) {
            long endTime = Instant.now().toEpochMilli();

            logger.error("Transaction Failed: ID={}, Error={}, ExecutionTime={}ms",
                    transactionId, e.getMessage(), (endTime - startTime), e);

            // 실패 이벤트 발행
            publishTransactionLog(transactionId, "FAILURE", "ACC", menuId, joinPoint.getSignature().toShortString(),
                    Arrays.toString(args), null, 500, e.getMessage(),
                    execUser, startTime, endTime);
            throw e;
        }
    }

    /**
     * 트랜잭션 ID 생성
     *
     * @return 트랜잭션 ID
     */
    private String generateTransactionId() {
        return "TRAN-" + UUID.randomUUID();
    }

    /**
     * 트랜잭션 로그 이벤트 발행.
     *
     * @param transactionId 트랜잭션 ID
     * @param logType       로그 유형 (SUCCESS/FAILURE)
     * @param moduleId      모듈 ID
     * @param menuId        메뉴 ID
     * @param uri           요청 URI
     * @param parameters    요청 파라미터
     * @param response      응답 데이터
     * @param responseCode  HTTP 응답 코드
     * @param errorMessage  오류 메시지
     * @param execUser      실행 사용자
     * @param startTime     시작 시간
     * @param endTime       종료 시간
     */
    private void publishTransactionLog(String transactionId, String logType, String moduleId, String menuId,
                                       String uri, String parameters, String response, int responseCode,
                                       String errorMessage, String execUser, long startTime, long endTime) {

        TransactionLogEvent event = new TransactionLogEvent(
                this,
                transactionId,
                logType,
                moduleId,
                menuId,
                uri,
                parameters,
                response,
                responseCode,
                errorMessage,
                execUser,
                startTime,
                endTime
        );
        eventPublisher.publishEvent(event);
    }

    /**
     * 메서드 인자에서 특정 파라미터 추출
     *
     * @param args      메서드 인자
     * @param paramName 파라미터 이름
     * @return 파라미터 값
     */
    private String extractParameter(Object[] args, String paramName) {
        if (args == null || args.length == 0) {
            return "N/A";
        }

        for (Object arg : args) {
            if (arg instanceof String && arg.toString().contains(paramName)) {
                return arg.toString(); // 필요시 추가 매핑 로직 적용 가능
            }
        }
        return "N/A";
    }
}
