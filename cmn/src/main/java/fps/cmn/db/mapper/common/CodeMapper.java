package fps.cmn.db.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * CodeMapper.java
 *
 * - 코드 데이터를 조회하는 MyBatis 매퍼 인터페이스
 * - 코드 테이블에서 모든 코드 데이터를 조회하거나 특정 코드 키로 조회하는 메서드를 제공합니다.
 */
@Mapper
public interface CodeMapper {
    /**
     * 모든 코드 데이터를 조회합니다.
     *
     * @return 코드 데이터 목록 (Map 형태로 반환)
     */
    List<Map<String, Object>> findAllCodes();

    /**
     * 특정 코드 키에 해당하는 코드 데이터를 조회합니다.
     *
     * @param codeKey 코드 키
     * @return 코드 데이터 (Map 형태로 반환)
     */
    Map<String, Object> findCodeByKey(@Param("codeKey") String codeKey);
}
