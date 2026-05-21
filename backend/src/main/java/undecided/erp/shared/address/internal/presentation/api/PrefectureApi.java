package undecided.erp.shared.address.internal.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.shared.address.spi.Prefecture;
import undecided.erp.shared.address.spi.PrefectureQuery;
import undecided.shared.common.exception.NotFoundBusinessException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prefectures")
public class PrefectureApi {
    /**
     * 都道府県情報を取得するためのクエリオブジェクト。
     *
     * <p>このフィールドは、{@link PrefectureApi} クラスにおいて都道府県データの検索処理を実行するために使用されます。
     * 主に {@link PrefectureQuery#findByCode(String)} メソッドを利用して、
     * 指定したコードに基づき該当する {@link Prefecture} エンティティを取得します。
     *
     * <p>注意点: このフィールドは不変であるため、初期化後に変更することはできません。
     */
    private final PrefectureQuery query;

    /**
     * 都道府県コードを指定して該当する都道府県情報を取得します。
     *
     * @param code 都道府県を一意に識別するためのコード
     * @return 指定されたコードに対応する都道府県情報
     * @throws NotFoundBusinessException 指定したコードに該当する都道府県が存在しない場合にスローされます
     */
    @GetMapping("/{code}")
    public Prefecture getPrefecture(@PathVariable String code) {
        return query
                .findByCode(code)
                .orElseThrow(() -> new NotFoundBusinessException("都道府県", "都道府県コード", code));
    }
}
