package fps.acc.common.exception;

import fps.cmn.common.logging.TransactionLogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * 글로벌 예외 처리 핸들러 클래스.
 * - MVC 레벨의 주요 예외를 처리하고 DB에 트랜잭션 로그를 기록합니다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 생성자.
     *
     * @param eventPublisher 이벤트 발행자 (트랜잭션 로그 저장용)
     */
    public GlobalExceptionHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * MissingServletRequestParameterException 처리.
     *
     * @param ex      누락된 파라미터 예외
     * @param request HTTP 요청 객체
     * @return 클라이언트에 반환할 HTTP 응답
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
        String missingParam = Optional.ofNullable(ex.getParameterName()).orElse("Unknown Parameter");
        String menuId = "default-menu";
        String requestUri = Optional.ofNullable(request.getDescription(false)).orElse("Unknown URI");
        String execUser = "Unknown User"; // 기본 사용자 정보

        logger.error("Missing required parameter: {}, Request Details: {}", missingParam, requestUri);

        // 트랜잭션 로그 발행
        publishTransactionLog(
                UUID.randomUUID().toString(),
                "FAILURE",
                "ACC",
                menuId,
                requestUri,
                null, // 요청 파라미터 없음
                null, // 응답 데이터 없음
                HttpStatus.BAD_REQUEST.value(),
                "Missing parameter: " + missingParam,
                execUser,
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameter: " + missingParam);
    }

    /**
     * 일반적인 예외 처리.
     *
     * @param ex      발생한 예외
     * @param request HTTP 요청 객체
     * @return 클라이언트에 반환할 HTTP 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex, WebRequest request) {
        String menuId = "default-menu";
        String requestUri = Optional.ofNullable(request.getDescription(false)).orElse("Unknown URI");
        String execUser = "Unknown User"; // 기본 사용자 정보

        logger.error("An error occurred: {}, Request Details: {}", ex.getMessage(), requestUri, ex);

        // 트랜잭션 로그 발행
        publishTransactionLog(
                UUID.randomUUID().toString(),
                "FAILURE",
                "ACC",
                menuId,
                requestUri,
                null, // 요청 파라미터 없음
                null, // 응답 데이터 없음
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                execUser,
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

    /**
     * 트랜잭션 로그 발행 메서드.
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
     * @param startTime     요청 시작 시간 (epoch milliseconds)
     * @param endTime       요청 종료 시간 (epoch milliseconds)
     */
    private void publishTransactionLog(String transactionId, String logType, String moduleId, String menuId,
                                       String uri, String parameters, String response,
                                       int responseCode, String errorMessage, String execUser,
                                       long startTime, long endTime) {
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
}
