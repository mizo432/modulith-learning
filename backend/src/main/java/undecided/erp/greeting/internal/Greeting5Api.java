package undecided.erp.greeting.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.shared.common.exception.BusinessException;
import undecided.shared.common.message.ResultMessages;

@RestController
@RequestMapping("/api/greeting5")
public class Greeting5Api {
  @GetMapping
  String get() {
    throw new BusinessException(ResultMessages.primary().add("CODE"));
    // return "Hello World";
  }
}
