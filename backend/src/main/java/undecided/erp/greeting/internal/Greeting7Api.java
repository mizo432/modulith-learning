package undecided.erp.greeting.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.shared.common.exception.SystemException;

@RestController
@RequestMapping("/api/greeting7")
public class Greeting7Api {

  @GetMapping
  String get() {
    throw new SystemException("System Error", "aaaaaaa", new RuntimeException("causeMessage"));
  }
}
