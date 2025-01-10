package fps.cmn.utils;

import fps.cmn.dto.HeaderDTO;

/**
 * 공통 유효성 검사 유틸리티.
 * HeaderDTO 객체의 유효성을 확인합니다.
 */
public class ValidationUtils {

    /**
     * HeaderDTO의 필수 필드를 검사합니다.
     *
     * @param header 검사할 HeaderDTO 객체
     * @throws IllegalArgumentException 필드 값이 유효하지 않을 경우 예외 발생
     */
    public static void validateHeader(HeaderDTO header) {
        if (header.getTransactionId() == null || header.getTransactionId().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID는 필수 값입니다.");
        }
        if (header.getInitialChannelCode() == null || header.getInitialChannelCode().isEmpty()) {
            throw new IllegalArgumentException("Initial Channel Code는 필수 값입니다.");
        }
        if (header.getChannelCode() == null || header.getChannelCode().isEmpty()) {
            throw new IllegalArgumentException("Channel Code는 필수 값입니다.");
        }
        if (header.getTimestamp() == null) {
            throw new IllegalArgumentException("Timestamp는 필수 값입니다.");
        }
    }
}
