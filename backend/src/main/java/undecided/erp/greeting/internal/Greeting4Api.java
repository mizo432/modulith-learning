package undecided.erp.greeting.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting4")
@RequiredArgsConstructor

public class Greeting4Api {

  private final ExceptionService exceptionService;

  @GetMapping
  String get() {
    throw exceptionService.throwLightException();

    // return "Hello World";
  }
}
