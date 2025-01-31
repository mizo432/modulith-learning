package undecided.erp.relationship.presentation.api;

public class EmployeeCriteria {

  public SearchType searchType() {
    return SearchType.BY_INITIALS;
  }

  enum SearchType {
    BY_NAME, BY_ID, BY_INITIALS
  }
}
