package fps.cmn.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * MessageMapper.java
 *
 * - 메시지 데이터를 조회하는 MyBatis 매퍼 인터페이스
 * - 메시지 테이블에서 모든 메시지 데이터를 조회하거나 특정 메시지 키로 조회하는 메서드를 제공합니다.
 */
@Mapper
public interface MessageMapper {
    /**
     * 모든 메시지 데이터를 조회합니다.
     *
     * @return 메시지 데이터 목록 (Map 형태로 반환)
     */
    List<Map<String, Object>> findAllMessages();

    /**
     * 특정 메시지 키에 해당하는 메시지 데이터를 조회합니다.
     *
     * @param messageKey 메시지 키
     * @return 메시지 데이터 (Map 형태로 반환)
     */
    Map<String, Object> findMessageByKey(@Param("messageKey") String messageKey);
}
