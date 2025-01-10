package fps.acc.common.exception;

import fps.cmn.common.logging.TransactionLogEvent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
import java.util.stream.Collectors;

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
        return processExceptionAndLog(ex, request, HttpStatus.BAD_REQUEST, "FAILURE");
    }

    /**
     * ConstraintViolationException 처리.
     *
     * @param ex      유효성 검증 실패 예외
     * @param request HTTP 요청 객체
     * @return 클라이언트에 반환할 HTTP 응답
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException ex, WebRequest request) {
        String errorDetails = ex.getConstraintViolations().stream()
                .map(this::formatConstraintViolation)
                .collect(Collectors.joining(", "));

        logger.error("Validation failed: {}, Request Details: {}", errorDetails, getRequestUri(request));

        return processExceptionAndLog(
                new Exception("Validation failed: " + errorDetails),
                request,
                HttpStatus.BAD_REQUEST,
                "FAILURE"
        );
    }

    /**
     * IllegalArgumentException 처리.
     *
     * @param ex      IllegalArgumentException 예외
     * @param request HTTP 요청 객체
     * @return 클라이언트에 반환할 HTTP 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return processExceptionAndLog(ex, request, HttpStatus.BAD_REQUEST, "FAILURE");
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
        return processExceptionAndLog(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE");
    }

    /**
     * 오류 메시지를 포맷하고 트랜잭션 로그를 기록합니다.
     *
     * @param ex      예외 객체
     * @param request HTTP 요청 객체
     * @param status  HTTP 상태 코드
     * @param logType 로그 유형
     * @return 클라이언트에 반환할 HTTP 응답
     */
    private ResponseEntity<String> processExceptionAndLog(Exception ex, WebRequest request, HttpStatus status, String logType) {
        String requestUri = getRequestUri(request);
        String execUser = "Unknown User";

        logger.error("Error occurred: {}, Request Details: {}", ex.getMessage(), requestUri);

        publishTransactionLog(
                UUID.randomUUID().toString(),
                logType,
                "ACC",
                "default-menu",
                requestUri,
                null, // 요청 파라미터 없음
                null, // 응답 데이터 없음
                status.value(),
                ex.getMessage(),
                execUser,
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli()
        );

        return ResponseEntity.status(status).body("Error: " + ex.getMessage());
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

    /**
     * ConstraintViolation 정보를 포맷팅합니다.
     *
     * @param violation 유효성 검증 실패 정보
     * @return 포맷된 오류 메시지
     */
    private String formatConstraintViolation(ConstraintViolation<?> violation) {
        return violation.getPropertyPath() + ": " + violation.getMessage();
    }

    /**
     * 요청 URI를 반환합니다.
     *
     * @param request WebRequest 객체
     * @return 요청 URI
     */
    private String getRequestUri(WebRequest request) {
        return Optional.ofNullable(request.getDescription(false)).orElse("Unknown URI");
    }
}
