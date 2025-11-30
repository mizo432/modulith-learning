package undecided.erp.shared.address.internal.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.common.exception.NotFoundBusinessException;
import undecided.erp.shared.address.spi.Prefecture;
import undecided.erp.shared.address.spi.PrefectureQuery;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prefectures")
public class PrefectureApi {
  private final PrefectureQuery query;

  @GetMapping("/{code}")
  public Prefecture getPrefecture(@PathVariable String code) {
    return query
        .findByCode(code)
        .orElseThrow(() -> new NotFoundBusinessException("都道府県", "都道府県コード", code));
  }
}
