package undecided.jobmanagement.jobexecution.internal;

import java.util.List;
import java.util.Optional;
import org.springframework.batch.core.job.Job;
import org.springframework.stereotype.Component;

/** ジョブコード（Spring Batch の Job 名）から実バッチの Job を解決するコンポーネント。 */
@Component
public class BatchJobResolver {

  private final List<Job> jobs;

  public BatchJobResolver(List<Job> jobs) {
    this.jobs = jobs;
  }

  /**
   * ジョブコードと Job 名が一致する Job を返します。
   *
   * @param jobCode ジョブコード（Spring Batch の Job 名）
   * @return 一致する Job。存在しない場合は空
   */
  public Optional<Job> resolve(String jobCode) {
    return jobs.stream().filter(job -> job.getName().equals(jobCode)).findFirst();
  }
}
