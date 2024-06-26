package undecided.erp.scrum.domain.model.team;

import undecided.erp.scrum.domain.model.product.ScrumBoard;

public class ScrumTeam {

  private final ScrumBoard board;

  public ScrumTeam() {
    board = new ScrumBoard();

  }

  public void doDailyStandup() {
    // team reports progress...
  }

  public void doRetrospective() {
    // team discusses successes and improvements...
  }

  // other scrum team methods...
}
