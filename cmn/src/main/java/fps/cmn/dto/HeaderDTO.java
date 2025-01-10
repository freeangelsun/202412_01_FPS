package fps.cmn.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 헤더 영역 DTO.
 * 모든 거래에서 공통적으로 필요한 헤더 정보를 정의합니다.
 */
@Data // Lombok을 사용해 Getter/Setter 자동 생성
public class HeaderDTO {
    @NotEmpty(message = "Transaction ID는 필수 값입니다.")
    private String transactionId;

    @NotEmpty(message = "Initial Channel Code는 필수 값입니다.")
    private String initialChannelCode;

    @NotEmpty(message = "Channel Code는 필수 값입니다.")
    private String channelCode;

    @NotNull(message = "Timestamp는 필수 값입니다.")
    private Long timestamp;
}
