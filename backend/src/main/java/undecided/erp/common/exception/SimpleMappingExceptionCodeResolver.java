package undecided.erp.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

@Setter
public class SimpleMappingExceptionCodeResolver implements ExceptionCodeResolver {

  private static final Logger logger = LoggerFactory.getLogger(
      SimpleMappingExceptionCodeResolver.class);
  private LinkedHashMap<String, String> exceptionMappings;
  private String defaultExceptionCode;

  public SimpleMappingExceptionCodeResolver() {
  }

  public String resolveExceptionCode(Exception ex) {
    if (ex == null) {
      logger.warn("target exception is null. return defaultExceptionCode.");
      return this.defaultExceptionCode;
    } else {
      if (ex instanceof ExceptionCodeProvider) {
        String code = ((ExceptionCodeProvider) ex).getCode();
        if (code != null) {
          return code;
        }
      }

      if (!CollectionUtils.isEmpty(this.exceptionMappings)) {
        for (Map.Entry<String, String> entry : this.exceptionMappings.entrySet()) {
          String targetException = entry.getKey();

          for (Class<?> exceptionClass = ex.getClass(); exceptionClass != Object.class;
              exceptionClass = exceptionClass.getSuperclass()) {
            if (exceptionClass.getName().contains(targetException)) {
              return entry.getValue();
            }
          }
        }

      }
      return this.defaultExceptionCode;
    }
  }
}
