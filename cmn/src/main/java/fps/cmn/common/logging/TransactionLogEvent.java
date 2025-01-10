package fps.cmn.common.logging;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 트랜잭션 로그 이벤트 클래스.
 * - 로그 데이터를 DB에 저장하기 위한 이벤트 정의.
 */
@Getter
public class TransactionLogEvent extends ApplicationEvent {

    // Getter 메서드
    private final String transactionId; // 트랜잭션 ID
    private final String logType; // 로그 유형 (SUCCESS/FAILURE)
    private final String moduleId; // 모듈 ID (예: ACC, CMN)
    private final String menuId; // 메뉴 ID
    private final String uri; // 요청 URI
    private final String parameters; // 요청 파라미터
    private final String response; // 응답 데이터
    private final int responseCode; // HTTP 응답 코드
    private final String errorMessage; // 오류 메시지
    private final String execUser; // 실행 사용자
    private final long startTime; // 시작 시간
    private final long endTime; // 종료 시간

    /**
     * 생성자: 이벤트 필드 초기화.
     *
     * @param source       이벤트 소스
     * @param transactionId 트랜잭션 ID
     * @param logType      로그 유형
     * @param moduleId     모듈 ID
     * @param menuId       메뉴 ID
     * @param uri          요청 URI
     * @param parameters   요청 파라미터
     * @param response     응답 데이터
     * @param responseCode HTTP 응답 코드
     * @param errorMessage 오류 메시지
     * @param execUser     실행 사용자
     * @param startTime    시작 시간
     * @param endTime      종료 시간
     */
    public TransactionLogEvent(Object source, String transactionId, String logType, String moduleId, String menuId,
                               String uri, String parameters, String response, int responseCode,
                               String errorMessage, String execUser, long startTime, long endTime) {
        super(source);
        this.transactionId = transactionId;
        this.logType = logType;
        this.moduleId = moduleId;
        this.menuId = menuId;
        this.uri = uri;
        this.parameters = parameters;
        this.response = response;
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
        this.execUser = execUser;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
