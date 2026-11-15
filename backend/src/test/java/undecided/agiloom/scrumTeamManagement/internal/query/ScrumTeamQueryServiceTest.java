package undecided.agiloom.scrumTeamManagement.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import undecided.agiloom.scrumTeamManagement.internal.ScrumTeamRepository;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeamStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("ScrumTeamQueryServiceのテスト")
class ScrumTeamQueryServiceTest {

  @Mock private ScrumTeamRepository scrumTeamRepository;

  @InjectMocks private ScrumTeamQueryService service;

  private SnowflakeId teamId;
  private UUID productId;
  private ScrumTeam scrumTeam;

  @BeforeEach
  void setUp() {
    teamId = SnowflakeId.of(1L);
    productId = UUID.randomUUID();
    scrumTeam =
        ScrumTeam.builder()
            .id(teamId)
            .productId(productId)
            .teamName("Frontend Team")
            .businessDomain("Web")
            .description("フロントエンドチーム")
            .status(ScrumTeamStatus.ACTIVE)
            .build();
  }

  @Nested
  @DisplayName("findByIdメソッドのテスト")
  class FindByIdTest {

    @Test
    @DisplayName("IDでスクラムチームが見つかる")
    void shouldFindById() {
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(scrumTeam));

      Optional<ScrumTeam> result = service.findById(teamId);

      assertThat(result).isPresent();
      assertThat(result.get().getTeamName()).isEqualTo("Frontend Team");
    }

    @Test
    @DisplayName("存在しないIDは空を返す")
    void shouldReturnEmptyWhenNotFound() {
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      Optional<ScrumTeam> result = service.findById(teamId);

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findAllメソッドのテスト")
  class FindAllTest {

    @Test
    @DisplayName("全スクラムチームが取得される")
    void shouldFindAll() {
      List<ScrumTeam> teams = List.of(scrumTeam);
      when(scrumTeamRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(teams));

      List<ScrumTeam> result = service.findAll(Pageable.unpaged());

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getTeamName()).isEqualTo("Frontend Team");
    }
  }

  @Nested
  @DisplayName("findByProductIdメソッドのテスト")
  class FindByProductIdTest {

    @Test
    @DisplayName("ProductIDでスクラムチームが取得される")
    void shouldFindByProductId() {
      List<ScrumTeam> teams = List.of(scrumTeam);
      when(scrumTeamRepository.findByProductIdAndDeletedAtIsNull(productId)).thenReturn(teams);

      List<ScrumTeam> result = service.findByProductId(productId);

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getProductId()).isEqualTo(productId);
    }
  }

  @Nested
  @DisplayName("findByStatusメソッドのテスト")
  class FindByStatusTest {

    @Test
    @DisplayName("ステータスでスクラムチームが取得される")
    void shouldFindByStatus() {
      List<ScrumTeam> teams = List.of(scrumTeam);
      when(scrumTeamRepository.findByStatus(ScrumTeamStatus.ACTIVE)).thenReturn(teams);

      List<ScrumTeam> result = service.findByStatus(ScrumTeamStatus.ACTIVE);

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getStatus()).isEqualTo(ScrumTeamStatus.ACTIVE);
    }
  }
}
