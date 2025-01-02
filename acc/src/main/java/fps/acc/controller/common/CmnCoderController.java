package fps.acc.controller.common;

import fps.cmn.service.common.CodeCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CmnCoderController.java
 *
 * - ACC 모듈에서 CMN 모듈의 CodeCacheService를 직접 호출하여 데이터를 반환합니다.
 */
@RestController
@RequestMapping("/fps/codes")
public class CmnCoderController {

    private final CodeCacheService codeCacheService;

    @Autowired
    public CmnCoderController(CodeCacheService codeCacheService) {
        this.codeCacheService = codeCacheService;
    }

    /**
     * 모든 코드 데이터를 반환합니다.
     *
     * @return 코드 데이터 목록
     */
    @GetMapping
    public List<Map<String, Object>> getAllCodes() {
        return codeCacheService.getAllCodes();
    }

    /**
     * 특정 코드 키로 코드 데이터를 반환합니다.
     *
     * @param codeKey 코드 키
     * @return 코드 데이터
     */
    @GetMapping("/{codeKey}")
    public Map<String, Object> getCodeByKey(@PathVariable String codeKey) {
        return codeCacheService.getCodeByKey(codeKey);
    }
}
