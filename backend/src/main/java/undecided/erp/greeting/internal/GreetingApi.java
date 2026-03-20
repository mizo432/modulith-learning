package undecided.erp.greeting.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.shared.common.exception.BusinessException;
import undecided.shared.common.message.ResultMessages;

@RestController
@RequestMapping("/api/greeting")
public class GreetingApi {
  @GetMapping
  String get() {
    throw new BusinessException(ResultMessages.info().add("CODE"));
    // return "Hello World";
  }
}
