package fps.cmn.mapper.common.logging;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 트랜잭션 로그 관리를 위한 매퍼 인터페이스.
 */
@Mapper
public interface TransactionLogMapper {

    /**
     * TRAN_LOG 테이블에 로그를 삽입합니다.
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
     * @param startTime     시작 시간
     * @param endTime       종료 시간
     * @param logType       로그 유형 (SUCCESS/FAILURE)
     */
    void insertTransactionLog(@Param("transactionId") String transactionId,
                              @Param("moduleId") String moduleId,
                              @Param("menuId") String menuId,
                              @Param("uri") String uri,
                              @Param("parameters") String parameters,
                              @Param("response") String response,
                              @Param("responseCode") int responseCode,
                              @Param("errorMessage") String errorMessage,
                              @Param("execUser") String execUser,
                              @Param("startTime") long startTime,
                              @Param("endTime") long endTime,
                              @Param("logType") String logType);

    /**
     * TRAN_LOG_DTL 테이블에 상세 로그를 삽입합니다.
     *
     * @param transactionId 트랜잭션 ID
     * @param detailKey     상세 키
     * @param detailValue   상세 값
     */
    void insertTransactionLogDetail(@Param("transactionId") String transactionId,
                                    @Param("detailKey") String detailKey,
                                    @Param("detailValue") String detailValue);
}
