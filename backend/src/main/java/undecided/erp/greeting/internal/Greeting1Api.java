package undecided.erp.greeting.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.shared.common.exception.BusinessException;
import undecided.shared.common.message.ResultMessages;

@RestController
@RequestMapping("/api/greeting1")
public class Greeting1Api {
  @GetMapping
  String get() {
    throw new BusinessException(ResultMessages.danger().add("CODE"));
  }
}
