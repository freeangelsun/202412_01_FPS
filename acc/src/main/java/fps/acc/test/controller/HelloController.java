package fps.acc.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acc")
public class HelloController {

    @GetMapping("/test")
    public String sayHello() {
        return "Hello, World!";
    }
}