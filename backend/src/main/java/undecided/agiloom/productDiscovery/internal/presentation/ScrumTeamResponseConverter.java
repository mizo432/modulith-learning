package undecided.agiloom.productDiscovery.internal.presentation;

import org.springframework.stereotype.Component;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;

/**
 * ScrumTeamエンティティをResponse DTOに変換するコンバーターです。
 */
@Component
public class ScrumTeamResponseConverter {

  public ScrumTeamResponse toScrumTeamResponse(ScrumTeam scrumTeam) {
    return ScrumTeamResponse.builder()
        .id(scrumTeam.getId())
        .name(scrumTeam.getName())
        .businessDomain(scrumTeam.getBusinessDomain())
        .poEmployeeId(scrumTeam.getPoEmployeeId())
        .poName(scrumTeam.getPoName())
        .smEmployeeId(scrumTeam.getSmEmployeeId())
        .smName(scrumTeam.getSmName())
        .memberEmployeeIds(scrumTeam.getMemberEmployeeIds())
        .productId(
            scrumTeam.getProduct() != null ? String.valueOf(scrumTeam.getProduct().getId()) : null)
        .createdAt(scrumTeam.getCreatedAt())
        .updatedAt(scrumTeam.getUpdatedAt())
        .build();
  }
}
