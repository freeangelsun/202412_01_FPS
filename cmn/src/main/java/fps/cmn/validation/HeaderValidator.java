package fps.cmn.validation;

import fps.cmn.dto.HeaderDTO;
import fps.cmn.utils.ValidationUtils;

/**
 * 헤더 검증 클래스.
 * HeaderDTO의 유효성을 검증하는 로직을 제공합니다.
 */
public class HeaderValidator {

    /**
     * HeaderDTO 유효성 검증.
     *
     * @param header 검증할 HeaderDTO 객체
     */
    public void validate(HeaderDTO header) {
        ValidationUtils.validateHeader(header);
    }
}
