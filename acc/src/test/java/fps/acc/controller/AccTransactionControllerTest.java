package fps.acc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * AOP 및 이벤트 기반 성공 트랜잭션 로그 테스트.
     */
    @Test
    void testSuccessfulTransaction() throws Exception {
        mockMvc.perform(get("/acc/tran/success") // GET 방식으로 변경
                        .param("menuId", "MENU123") // 쿼리 파라미터 추가
                        .param("execUser", "testUser")
                        .contentType(MediaType.APPLICATION_JSON)) // Content-Type 설정
                .andExpect(status().isOk()); // 기대 상태 코드: 200

    }
/*
    @Test
    void testFailedTransaction() throws Exception {
        mockMvc.perform(get("/acc/tran/failure")
                        .param("menuId", "MENU123")
                        .param("execUser", "testUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // 요청 및 응답 로그 출력
                .andExpect(status().isInternalServerError()); // 기대 상태 코드: 500
    }

*/
}
