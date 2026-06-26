package undecided.erp.greeting.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting2")
@RequiredArgsConstructor

public class Greeting2Api {

  private final ExceptionService exceptionService;

  @GetMapping
  String get() {
    throw exceptionService.throwErrorException();

    // return "Hello World";
  }
}
