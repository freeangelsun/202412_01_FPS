package fps.cmn.service.common.logging;

import fps.cmn.mapper.common.logging.TransactionLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 트랜잭션 로그를 DB에 저장하기 위한 서비스 클래스.
 * TRAN_LOG와 TRAN_LOG_DTL 테이블에 로그를 저장합니다.
 */
@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionLogMapper logMapper;

    /**
     * TRAN_LOG와 TRAN_LOG_DTL 테이블에 트랜잭션 로그를 저장합니다.
     *
     * @param transactionId 트랜잭션 ID
     * @param moduleId      모듈 ID
     * @param menuId        메뉴 ID
     * @param uri           요청 URI
     * @param parameters    요청 파라미터
     * @param response      응답 데이터
     * @param responseCode  응답 코드
     * @param errorMessage  오류 메시지
     * @param execUser      수행 사용자
     * @param startTime     시작 시간 (밀리초 단위)
     * @param endTime       종료 시간 (밀리초 단위)
     * @param logType       로그 유형 (SUCCESS/FAILURE)
     */
    public void saveTransactionLog(String transactionId, String moduleId, String menuId, String uri, String parameters,
                                   String response, int responseCode, String errorMessage, String execUser,
                                   long startTime, long endTime, String logType) {
        // 밀리초를 초로 변환하여 TIMESTAMP 형식에 맞춥니다.
        long startTimeInSeconds = startTime / 1000;
        long endTimeInSeconds = endTime / 1000;

        // TRAN_LOG 테이블에 삽입
        logMapper.insertTransactionLog(transactionId, moduleId, menuId, uri, parameters, response,
                responseCode, errorMessage, execUser, startTimeInSeconds, endTimeInSeconds, logType);

        // TRAN_LOG_DTL에 상세 정보를 삽입
        logMapper.insertTransactionLogDetail(transactionId, "ExecutionStage", "Transaction started");
        logMapper.insertTransactionLogDetail(transactionId, "Parameters", parameters);
        if (errorMessage != null) {
            logMapper.insertTransactionLogDetail(transactionId, "Error", errorMessage);
        }
    }
}
