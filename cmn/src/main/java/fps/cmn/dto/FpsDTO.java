package fps.cmn.dto;

import jakarta.validation.Valid;
import lombok.Data;

/**
 * 클래스: FpsDTO
 *
 * 모든 거래 요청의 공통 구조를 정의합니다.
 * @param <T> 데이터 타입 (예: ACC 모듈의 DTO)
 */
@Data
public class FpsDTO<T> {

    @Valid
    private HeaderDTO header; // 공통 헤더 정보

    @Valid
    private DataDTO<T> data; // 데이터 정보 (제네릭 타입)
}
