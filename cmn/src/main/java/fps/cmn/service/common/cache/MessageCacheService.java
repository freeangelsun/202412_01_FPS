package fps.cmn.service.common.cache;

import fps.cmn.mapper.common.MessageMapper;
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
 * MessageCacheService.java
 *
 * - 메시지 데이터를 캐싱하여 애플리케이션 성능을 향상시킵니다.
 * - 애플리케이션 시작 시 전체 메시지 데이터를 자동으로 캐싱합니다.
 */
@Service
public class MessageCacheService {

    private static final Logger logger = LoggerFactory.getLogger(MessageCacheService.class);
    private final MessageMapper messageMapper;

    public MessageCacheService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    /**
     * 모든 메시지 데이터를 캐싱합니다.
     *
     * @return 메시지 데이터 목록
     */
    @Cacheable("messageCache")
    public List<Map<String, Object>> getAllMessages() {
        logger.info("Cache Miss: Fetching all messages from database");
        return messageMapper.findAllMessages();
    }

    /**
     * 특정 메시지 키에 해당하는 메시지 데이터를 반환합니다.
     *
     * @param messageKey 메시지 키
     * @return 메시지 데이터
     */
    @Cacheable(value = "messageCache", key = "#messageKey")
    public Map<String, Object> getMessageByKey(String messageKey) {
        logger.debug("Cache Miss: Fetching message for key: {}", messageKey);
        return messageMapper.findMessageByKey(messageKey);
    }

    /**
     * 캐시를 리로딩합니다.
     *
     * @return 최신 메시지 데이터 목록
     */
    @CachePut("messageCache")
    public List<Map<String, Object>> reloadMessages() {
        logger.info("Cache Reload: Fetching updated messages from database");
        return messageMapper.findAllMessages();
    }

    /**
     * 애플리케이션 시작 시 전체 메시지 데이터를 자동으로 캐싱합니다.
     */
    @PostConstruct
    public void loadCacheOnStartup() {
        logger.info("Initializing message cache at startup");
        getAllMessages();
    }

    /**
     * 주기적으로 캐시를 리로딩합니다.
     * - 30분마다 자동으로 실행됩니다.
     */
    @Scheduled(fixedRate = 1800000)
    public void scheduledReloadMessages() {
        logger.info("Scheduled cache reload triggered");
        reloadMessages();
    }
}
