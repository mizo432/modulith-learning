package undecided.erp.addressMgmt.model.machiAza;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@RequiredArgsConstructor
@Getter
public enum MachiAzaStatus {
  自治体確認待ち(1), 地方自治法の町若しくは字に該当(2), 地方自治法の町若しくは字に非該当(3), 不明(
      9);

  private final Integer id;

  public static MachiAzaStatus valueOfId(Integer id) {
    return Arrays.stream(values())
        .filter(machiAzaStatus -> machiAzaStatus.getId().equals(id))
        .findFirst()
        .orElse(不明);
  }
}
