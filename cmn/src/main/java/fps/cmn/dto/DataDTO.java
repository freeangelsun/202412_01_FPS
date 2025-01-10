package fps.cmn.dto;

import jakarta.validation.Valid;
import lombok.Data;

/**
 * 클래스: DataDTO
 *
 * 데이터 요청의 공통 구조를 정의합니다.
 * @param <T> 데이터 타입 (예: ACC 모듈의 DTO)
 */
@Data
public class DataDTO<T> {
    @Valid
    private T body; // 데이터 본문 (제네릭 타입)
}
