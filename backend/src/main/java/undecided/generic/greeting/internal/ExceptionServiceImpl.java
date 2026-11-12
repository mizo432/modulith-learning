package undecided.generic.greeting.internal;

import com.github.mizo432.shared.common.exception.BusinessException;
import com.github.mizo432.shared.common.exception.SystemException;
import com.github.mizo432.shared.common.message.ResultMessages;
import org.springframework.stereotype.Service;

@Service
public class ExceptionServiceImpl implements ExceptionService {

  @Override
  public RuntimeException throwDangerException() {
    throw new BusinessException(ResultMessages.danger().add("e.xx.yy.3001"));
  }

  @Override
  public RuntimeException throwSystemException() {
    throw new SystemException("e.xx.yy.3001", "aaaaaaa", new RuntimeException("causeMessage"));
  }

  @Override
  public RuntimeException throwDarkException() {
    throw new BusinessException(ResultMessages.dark().add("e.xx.yy.3001"));
  }

  @Override
  public RuntimeException throwPrimaryException() {
    throw new BusinessException(ResultMessages.primary().add("e.xx.yy.3001"));
  }

  @Override
  public RuntimeException throwLightException() {
    throw new BusinessException(ResultMessages.light().add("e.xx.yy.3001"));
  }

  @Override
  public RuntimeException throwSecondaryException() {
    throw new BusinessException(ResultMessages.secondary().add("e.xx.yy.3001"));
  }

  @Override
  public RuntimeException throwErrorException() {
    throw new BusinessException(ResultMessages.error().add("e.xx.yy.3001"));
  }
}
