package undecided.agiloom.productDiscovery.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.internal.ScrumTeamRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("CreateScrumTeamCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateScrumTeamCommandServiceTest {

  @Mock private ScrumTeamRepository scrumTeamRepository;

  @Mock private ProductRepository productRepository;

  private CreateScrumTeamCommandService service;

  @BeforeEach
  void setUp() {
    service = new CreateScrumTeamCommandService(scrumTeamRepository, productRepository);
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("ScrumTeamを作成してRepositoryに保存し、IDを返すこと")
    void shouldCreateAndSaveScrumTeam() {
      // Arrange
      SnowflakeId productId = SnowflakeId.of(1L);
      Product product =
          Product.builder().id(productId).name("Test Product").build();

      CreateScrumTeamCommand command =
          CreateScrumTeamCommand.builder()
              .productId(productId)
              .name("Test Team")
              .businessDomain("Test Domain")
              .build();

      ScrumTeam savedTeam =
          ScrumTeam.builder()
              .id(SnowflakeId.of(2L))
              .name("Test Team")
              .businessDomain("Test Domain")
              .product(product)
              .build();

      when(productRepository.findById(productId)).thenReturn(Optional.of(product));
      when(scrumTeamRepository.save(any(ScrumTeam.class))).thenReturn(savedTeam);

      // Act
      SnowflakeId result = service.execute(command);

      // Assert
      assertThat(result.value()).isEqualTo(2L);
      verify(scrumTeamRepository).save(any(ScrumTeam.class));
    }

    @Test
    @DisplayName("Productが見つからない場合、例外がスローされること")
    void shouldThrowExceptionWhenProductNotFound() {
      // Arrange
      SnowflakeId productId = SnowflakeId.of(1L);
      CreateScrumTeamCommand command =
          CreateScrumTeamCommand.builder()
              .productId(productId)
              .name("Test Team")
              .businessDomain("Test Domain")
              .build();

      when(productRepository.findById(productId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Product not found");
    }
  }
}
