package fps.acc.common.aop;

import fps.cmn.dto.FpsDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * AOP를 사용하여 모든 컨트롤러 요청에서 FpsDTO 규격을 검증합니다.
 * 요청이 FpsDTO 규격에 맞지 않으면 예외를 발생시킵니다.
 */
@Aspect
@Component
public class FpsValidationAspect {

    private final Validator validator;

    /**
     * 생성자.
     *
     * @param validator Validator 인스턴스
     */
    public FpsValidationAspect(Validator validator) {
        this.validator = validator;
    }

    /**
     * 모든 컨트롤러 요청에서 FpsDTO를 검증.
     *
     * @param joinPoint AOP 조인포인트
     * @return 메서드 실행 결과
     * @throws Throwable 검증 실패 시 예외 발생
     */
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object validateFpsRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (!(arg instanceof FpsDTO)) {
                throw new IllegalArgumentException("요청은 반드시 FpsDTO 형식이어야 합니다.");
            }

            FpsDTO<?> fpsDTO = (FpsDTO<?>) arg;

            // Header와 Data 필드 검증
            if (fpsDTO.getHeader() == null || fpsDTO.getData() == null) {
                throw new IllegalArgumentException("FpsDTO 구조가 올바르지 않습니다. Header 또는 Data가 누락되었습니다.");
            }

            // Bean Validation 수행
            Set<ConstraintViolation<FpsDTO<?>>> violations = validator.validate(fpsDTO);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException("FpsDTO 유효성 검증 실패", violations);
            }
        }

        return joinPoint.proceed();
    }
}
