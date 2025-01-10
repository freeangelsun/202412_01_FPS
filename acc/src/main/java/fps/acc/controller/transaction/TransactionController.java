package fps.acc.controller.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/acc/tran")
public class TransactionController {

    @GetMapping("/success")
    public ResponseEntity<String> handleSuccessfulTransaction(@RequestParam String menuId, @RequestParam String execUser) {
        return ResponseEntity.ok("Transaction processed successfully.");
    }

    @GetMapping("/failure")
    public ResponseEntity<String> handleFailedTransaction(@RequestParam String menuId, @RequestParam String execUser) {
        throw new RuntimeException("Simulated transaction failure.");
    }
}
