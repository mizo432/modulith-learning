package undecided.generic.greeting.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting1")
@RequiredArgsConstructor
public class Greeting1QueryApi {

  private final ExceptionService exceptionService;

  @GetMapping
  String get() {
    throw exceptionService.throwDangerException();
  }
}
