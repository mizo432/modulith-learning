package undecided.erp.greeting.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.shared.common.exception.BusinessException;
import undecided.shared.common.message.ResultMessages;

@RestController
@RequestMapping("/api/greeting4")
public class Greeting4Api {
  @GetMapping
  String get() {
    throw new BusinessException(ResultMessages.light().add("CODE"));
    // return "Hello World";
  }
}
