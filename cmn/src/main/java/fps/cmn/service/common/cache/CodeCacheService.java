package fps.cmn.service.common.cache;

import fps.cmn.mapper.common.CodeMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * CodeCacheService.java
 *
 * - 코드 데이터를 캐싱하여 애플리케이션 성능을 향상시킵니다.
 * - 애플리케이션 시작 시 전체 코드 데이터를 자동으로 캐싱합니다.
 */
@Service
public class CodeCacheService {

    private static final Logger logger = LoggerFactory.getLogger(CodeCacheService.class);
    private final CodeMapper codeMapper;

    public CodeCacheService(CodeMapper codeMapper) {
        this.codeMapper = codeMapper;
    }

    /**
     * 모든 코드 데이터를 캐싱합니다.
     *
     * @return 코드 데이터 목록
     */
    @Cacheable("codeCache")
    public List<Map<String, Object>> getAllCodes() {
        logger.info("Cache Miss: Fetching all codes from database");
        return codeMapper.findAllCodes();
    }

    /**
     * 특정 코드 키에 해당하는 코드 데이터를 반환합니다.
     *
     * @param codeKey 코드 키
     * @return 코드 데이터
     */
    @Cacheable(value = "codeCache", key = "#codeKey")
    public Map<String, Object> getCodeByKey(String codeKey) {
        logger.debug("Cache Miss: Fetching code for key: {}", codeKey);
        return codeMapper.findCodeByKey(codeKey);
    }

    /**
     * 캐시를 리로딩합니다.
     *
     * @return 최신 코드 데이터 목록
     */
    @CachePut("codeCache")
    public List<Map<String, Object>> reloadCodes() {
        logger.info("Cache Reload: Fetching updated codes from database");
        return codeMapper.findAllCodes();
    }

    /**
     * 애플리케이션 시작 시 전체 코드 데이터를 자동으로 캐싱합니다.
     */
    @PostConstruct
    public void loadCacheOnStartup() {
        logger.info("Initializing code cache at startup");
        getAllCodes();
    }

    /**
     * 주기적으로 캐시를 리로딩합니다.
     * - 30분마다 자동으로 실행됩니다.
     */
    @Scheduled(fixedRate = 1800000)
    public void scheduledReloadCodes() {
        logger.info("Scheduled cache reload triggered");
        reloadCodes();
    }
}
