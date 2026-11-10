package undecided.agiloom.sprintExecution.internal.presentation;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintDTO;

/**
 * SprintドメインオブジェクトとSprintDTOを変換するアセンブラです。
 * <p>
 * Controller間の依存関係を解消し、変換ロジックを独立したクラスに分離します。
 * </p>
 */
@Component
public class SprintResponseAssembler {

    /**
     * SprintエンティティをSprintDTOに変換します。
     *
     * @param sprint Sprintエンティティ
     * @return SprintDTO
     */
    public SprintDTO toDTO(@NonNull Sprint sprint) {
        return SprintDTO.builder()
            .id(sprint.getId())
            .productId(sprint.getProductId())
            .sprintNumber(sprint.getSprintNumber())
            .title(sprint.getTitle())
            .goal(sprint.getGoal())
            .startDate(sprint.getStartDate())
            .endDate(sprint.getEndDate())
            .status(sprint.getStatus())
            .teamId(sprint.getTeamId())
            .teamName(sprint.getTeamName())
            .velocity(sprint.getVelocity())
            .notes(sprint.getNotes())
            .createdAt(sprint.getCreatedAt())
            .updatedAt(sprint.getUpdatedAt())
            .build();
    }

    /**
     * SprintDTOをSprintResponseに変換します。
     *
     * @param sprint SprintDTO
     * @return SprintResponse DTO
     */
    public SprintResponse toResponse(@NonNull SprintDTO sprint) {
        return SprintResponse.builder()
                .id(sprint.getId())
                .productId(sprint.getProductId())
                .sprintNumber(sprint.getSprintNumber())
                .title(sprint.getTitle())
                .goal(sprint.getGoal())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .status(sprint.getStatus())
                .teamId(sprint.getTeamId())
                .teamName(sprint.getTeamName())
                .velocity(sprint.getVelocity())
                .notes(sprint.getNotes())
                .createdAt(sprint.getCreatedAt())
                .updatedAt(sprint.getUpdatedAt())
                .build();
    }
}
