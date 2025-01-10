package fps.cmn.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ServiceAccessAspect
 * - 서브 모듈 간 서비스 직접 호출을 방지하고, 컨트롤러를 통한 호출만 허용하는 AOP 클래스.
 * - CMN 모듈의 서비스는 모든 모듈에서 자유롭게 호출 가능.
 */
@Aspect
@Component
public class ServiceAccessAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAccessAspect.class);

    /**
     * 서비스 직접 호출 방지.
     * - fps..service 패키지의 모든 클래스에 적용.
     * - CMN 모듈의 서비스 패키지 (fps.cmn.service)만 예외 처리.
     */
    @Before("within(fps..service..*) && !within(fps.cmn.service..*)")
    public void preventDirectServiceAccess() {
        // 호출한 클래스가 Controller인지 확인
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        boolean isControllerCall = false;

        for (StackTraceElement element : stackTrace) {
            // 컨트롤러에서 호출되었는지 확인
            if (element.getClassName().contains(".controller.")) {
                isControllerCall = true;
                break;
            }

            /*
            // 동일 모듈 내 서비스 간 호출 확인 (허용)
            if (element.getClassName().contains(".service.") &&
                    element.getClassName().startsWith(getCurrentModule())) {
                isControllerCall = true;
                break;
            }
             */
        }


        // Controller가 아닌 경우 예외 발생
        if (!isControllerCall) {
            logger.error("서비스 직접 호출이 감지되었습니다. 컨트롤러를 통해서만 접근 가능합니다.");
            throw new SecurityException("Direct service access is not allowed. Use controllers to access services.");
        }
    }

    /**
     * 현재 모듈 이름 반환.
     * - 패키지 이름을 기반으로 모듈 식별.
     *
     * @return 현재 모듈 이름
     */
    private String getCurrentModule() {
        String packageName = this.getClass().getPackageName();
        return packageName.substring(0, packageName.indexOf(".service"));
    }
}
