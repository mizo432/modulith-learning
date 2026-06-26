package undecided.erp.greeting.internal;

import org.springframework.stereotype.Service;
import undecided.shared.common.exception.BusinessException;
import undecided.shared.common.exception.SystemException;
import undecided.shared.common.message.ResultMessages;

@Service
public class ExceptionServicezimpl implements ExceptionService {

  @Override
  public RuntimeException throwDangerException() {
    throw new BusinessException(ResultMessages.danger().add("CODE"));
  }

  @Override
  public RuntimeException throwSystemException() {
    throw new SystemException("System Error", "aaaaaaa", new RuntimeException("causeMessage"));
  }

  @Override
  public RuntimeException throwDarkException() {
    throw new BusinessException(ResultMessages.dark().add("CODE"));

  }

  @Override
  public RuntimeException throwPrimaryException() {
    throw new BusinessException(ResultMessages.primary().add("CODE"));

  }

  @Override
  public RuntimeException throwLightException() {
    throw new BusinessException(ResultMessages.light().add("CODE"));
  }

  @Override
  public RuntimeException throwSecondaryException() {
    throw new BusinessException(ResultMessages.secondary().add("CODE"));

  }

  @Override
  public RuntimeException throwErrorException() {
    throw new BusinessException(ResultMessages.error().add("CODE"));
  }
}
