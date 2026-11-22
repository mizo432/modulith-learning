package undecided.notification.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
class NotificationScrumTeamMemberRepositoryImpl implements NotificationScrumTeamMemberRepository {

  private final NotificationScrumTeamSubsetRepository scrumTeamRepository;
  private final NotificationEmployeeEmailSubsetRepository emailRepository;
  private final NotificationCommentParticipantSubsetRepository participantRepository;

  NotificationScrumTeamMemberRepositoryImpl(
      NotificationScrumTeamSubsetRepository scrumTeamRepository,
      NotificationEmployeeEmailSubsetRepository emailRepository,
      NotificationCommentParticipantSubsetRepository participantRepository) {
    this.scrumTeamRepository = scrumTeamRepository;
    this.emailRepository = emailRepository;
    this.participantRepository = participantRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Set<NotificationScrumTeamMember> findByScrumTeamId(SnowflakeId scrumTeamId) {
    return scrumTeamRepository.findById(scrumTeamId)
        .map(subset -> membersWithEmail(subset.getMemberEmployeeIds()))
        .orElseGet(Set::of);
  }

  private Set<NotificationScrumTeamMember> membersWithEmail(List<SnowflakeId> memberIds) {
    if (memberIds.isEmpty()) {
      return Set.of();
    }
    Map<SnowflakeId, NotificationCommentParticipantSubset> participants = new HashMap<>();
    for (NotificationCommentParticipantSubset participant :
        participantRepository.findByEmployeeIdIn(memberIds)) {
      if (participant.getEmployeeEmail() != null && !participant.getEmployeeEmail().isBlank()) {
        participants.merge(
            participant.getEmployeeId(), participant,
            (first, second) -> first.getCreatedAt().isAfter(second.getCreatedAt()) ? first : second);
      }
    }
    Map<SnowflakeId, String> emails = new HashMap<>();
    participants.forEach((id, participant) -> emails.put(id, participant.getEmployeeEmail()));
    for (NotificationEmployeeEmailSubset email : emailRepository.findAllById(memberIds)) {
      emails.put(email.getId(), email.getEmail());
    }
    Set<NotificationScrumTeamMember> members = new HashSet<>();
    for (SnowflakeId id : memberIds) {
      members.add(new NotificationScrumTeamMember(id, emails.get(id)));
    }
    return members;
  }
}