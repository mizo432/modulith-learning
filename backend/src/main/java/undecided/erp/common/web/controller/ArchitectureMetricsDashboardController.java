package undecided.erp.common.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * システムアーキテクチャメトリクスを表示するダッシュボードのコントローラー。
 * <p>
 * このコントローラーは、Spring Modulithのアクチュエーターエンドポイントからアーキテクチャメトリクスを取得し、
 * ダッシュボードビューに表示します。
 */
@Controller
@RequestMapping("/dashboard/architecture")
public class ArchitectureMetricsDashboardController {

    private final RestTemplate restTemplate;

    @Autowired
    public ArchitectureMetricsDashboardController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * アーキテクチャメトリクスダッシュボードのメインページを表示します。
     *
     * @param model ビューに渡すモデル
     * @return ダッシュボードのビュー名
     */
    @GetMapping
    public String dashboard(Model model) {
        // アクチュエーターエンドポイントからモジュール情報を取得
        String modulithData = restTemplate.getForObject("http://localhost:8080/actuator/modulith", String.class);
        String modulithApplicationsData = restTemplate.getForObject("http://localhost:8080/actuator/modulithApplications", String.class);

        // モデルにデータを追加
        model.addAttribute("modulithData", modulithData);
        model.addAttribute("modulithApplicationsData", modulithApplicationsData);
        model.addAttribute("pageTitle", "システムアーキテクチャメトリクスダッシュボード");

        return "architecture-dashboard";
    }
}
