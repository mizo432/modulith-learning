package undecided.erp.greeting.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting")
public class GreetingApi {
  @GetMapping
  String get() {
    // throw new BusinessException(ResultMessages.warning().add("CODE"));
    return "Hello World";
  }
}
