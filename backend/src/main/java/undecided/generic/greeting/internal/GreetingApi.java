package undecided.generic.greeting.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting")
public class GreetingApi {
  @GetMapping
  String get() {
    return "Hello World";
  }
}
