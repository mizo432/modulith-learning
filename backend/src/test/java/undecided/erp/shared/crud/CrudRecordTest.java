package undecided.erp.shared.crud;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.crud.CrudRecord.CrudRecords;

class CrudRecordTest {

  @Nested
  class CreateDefaultCrudRecordTest {

    @Test
    void shouldCreateCrudRecordWithNoChangeCrudType() {
      // Initialize test data
      String testRecord = "Test Record";

      // Method call
      CrudRecord<String> result = CrudRecord.createDefaultCrudRecord(testRecord);

      // Assertion
      assertThat(result).as("Result should not be null").isNotNull();
      assertThat(result.getRecord()).as("Record should be same as test record")
          .isEqualTo(testRecord);
      assertThat(result.getCrudType()).as("Default CrudType should be NO_CHANGED")
          .isEqualTo(CrudType.NO_CHANGED);
    }

  }

  @Nested
  class CreateTest {

    @Test
    void shouldAssignCorrectCrudTypeToRecord() {
      // Initialize test data
      String testRecord = "Test Record";
      CrudType testCrudType = CrudType.CREATED;

      // Method call
      CrudRecord<String> result = CrudRecord.create(testRecord, testCrudType);

      // Assertions
      assertThat(result).as("Result should not be null").isNotNull();
      assertThat(result.getRecord()).as("Record should be same as test record")
          .isEqualTo(testRecord);
      assertThat(result.getCrudType()).as("CrudType should be same as test CrudType")
          .isEqualTo(testCrudType);
    }


    @Nested
    class CrudRecordsTest {

      @Nested
      class CreatedTest {

        @Test
        void shouldReturnEmptyListForNoCreatedCrudRecords() {
          // Initialize test data
          String record1 = "Record 1";
          String record2 = "Record 2";
          CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.UPDATED);
          CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
          List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

          // Method call
          List<String> createdRecords = CrudRecords.of(records).created();

          // Assertions
          assertThat(createdRecords).as("Created records should be empty").isEmpty();
        }

        @Test
        void shouldReturnListOfCreatedCrudRecords() {
          // Initialize test data
          String record1 = "Record 1";
          String record2 = "Record 2";
          CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
          CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.CREATED);
          List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

          // Method call
          List<String> createdRecords = CrudRecords.of(records).created();

          // Assertions
          assertThat(createdRecords).as("Created records should not be null").isNotNull();
          assertThat(createdRecords.size()).as("Should have two created records").isEqualTo(2);
          assertThat(createdRecords).as("Created records should match input records")
              .contains(record1, record2);
        }

        @Test
        void shouldFilterAndReturnOnlyCreatedCrudRecords() {
          // Initialize test data
          String record1 = "Record 1";
          String record2 = "Record 2";
          String record3 = "Record 3";
          String record4 = "Record 4";
          CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
          CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
          CrudRecord<String> crudRecord3 = CrudRecord.create(record3, CrudType.NO_CHANGED);
          CrudRecord<String> crudRecord4 = CrudRecord.create(record4, CrudType.DELETED);
          List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2, crudRecord3,
              crudRecord4);

          List<String> createdRecords = CrudRecords.of(records).created();

          // Assertions
          assertThat(createdRecords).as("Created records should not be null").isNotNull();
          assertThat(createdRecords.size()).as("Should have two created records").isEqualTo(1);
          assertThat(createdRecords).as("Created records should match input records")
              .contains(record1);
        }

      }

      @Nested
      class OfTest {

        @Test
        void shouldReturnOnlyCreatedRecords() {
          // Initialize test data
          String record1 = "Record 1";
          String record2 = "Record 2";
          CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
          CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
          List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

          // Method call
          List<String> createdRecords = CrudRecords.of(records).created();

          // Assertions
          assertThat(createdRecords).as("Created records should not be null").isNotNull();
          assertThat(createdRecords.size()).as("Should have only one created record").isEqualTo(1);
          assertThat(createdRecords.getFirst()).as("The created record should match input record")
              .isEqualTo(record1);
        }

        @Test
        void shouldReturnEmptyListIfNoCreateCrudRecords() {
          // Initialize test data

          // Method call
          CrudRecords<String> crudRecords = CrudRecords.of(null);

          // Assertions
          assertThat(crudRecords).as("Created records should not be null").isNotNull();
          assertThat(crudRecords.records()).as("Should have empty record").isEmpty();
        }
      }

    }
  }

  @Nested
  class UpdatedTest {

    @Test
    void shouldFilterAndReturnOnlyUpdatedCrudRecords() {
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.CREATED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

      // Method call
      List<String> updatedRecords = CrudRecords.of(records).updated();

      // Assertions
      assertThat(updatedRecords).as("Updated records should not be null").isNotNull();
      assertThat(updatedRecords).as("Should have only one updated record").isEmpty();
    }

    @Test
    void shouldFilterAndReturnOnlyUpdatedCrudRecords2() {
      // Initialize test data
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      String record3 = "Record 3";
      String record4 = "Record 4";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
      CrudRecord<String> crudRecord3 = CrudRecord.create(record3, CrudType.NO_CHANGED);
      CrudRecord<String> crudRecord4 = CrudRecord.create(record4, CrudType.DELETED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2, crudRecord3,
          crudRecord4);

      List<String> updatedRecords = CrudRecords.of(records).updated();

      // Assertions
      assertThat(updatedRecords).as("Updated records should not be null").isNotNull();
      assertThat(updatedRecords.size()).as("Should have only one updated record").isEqualTo(1);
      assertThat(updatedRecords).as("Updated records should not be null").contains(record2);

    }
  }

  @Nested
  class DeletedTest {

    @Test
    void shouldReturnOnlyDeletedRecords() {
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.DELETED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

      // Method call
      List<String> deletedRecords = CrudRecords.of(records).deleted();

      // Assertions
      assertThat(deletedRecords).as("Deleted records should not be null").isNotNull();
      assertThat(deletedRecords.size()).as("Should have only one deleted record").isEqualTo(1);
      assertThat(deletedRecords.getFirst()).as("The deleted record should match input record")
          .isEqualTo(record1);
    }

    @Test
    void shouldReturnOnlyDeletedRecords2() {
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      String record3 = "Record 3";
      String record4 = "Record 4";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
      CrudRecord<String> crudRecord3 = CrudRecord.create(record3, CrudType.NO_CHANGED);
      CrudRecord<String> crudRecord4 = CrudRecord.create(record4, CrudType.DELETED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2, crudRecord3,
          crudRecord4);

      List<String> deletedRecords = CrudRecords.of(records).deleted();

      // Assertions
      assertThat(deletedRecords).as("Deleted records should not be null").isNotNull();
      assertThat(deletedRecords.size()).as("Should have only one deleted record").isEqualTo(1);
      assertThat(deletedRecords.getFirst()).as("The deleted record should match input record")
          .isEqualTo(record4);
    }
  }

  @Nested
  class RecordsTest {

    @Test
    void shouldReturnTheSameRecordsThatWereGiven() {
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.UPDATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.CREATED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

      // Method call
      List<CrudRecord<String>> createdRecords = CrudRecords.of(records).records();

      // Assertions
      assertThat(createdRecords).as("Returned records should not be null").isNotNull();
      assertThat(createdRecords.size()).as(
          "Returned list size should be equal to the input list size").isEqualTo(2);
    }

  }

  @Nested
  class ChangedTest {

    @Test
    void shouldReturnNoChangedRecordsWhenThereAreNoChangedRecords() {
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.UPDATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.CREATED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

      // Method call
      List<String> changedRecords = CrudRecords.of(records).changed();

      // Assertions
      assertThat(changedRecords).as("Changed records should be empty").hasSize(2);
    }

    @Test
    void shouldReturnOnlyChangedRecords() {
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.NO_CHANGED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

      // Method call
      List<String> changedRecords = CrudRecords.of(records).changed();

      // Assertions
      assertThat(changedRecords).as("Changed records should not be null").isNotNull();
      assertThat(changedRecords.size()).as("Should have only one changed record").isEqualTo(1);
      assertThat(changedRecords.getFirst()).as("The changed record should match input record")
          .isEqualTo(record1);
    }

    @Test
    void shouldReturnOnlyChangedRecords2() {
      // Initialize test data
      String record1 = "Record 1";
      String record2 = "Record 2";
      String record3 = "Record 3";
      String record4 = "Record 4";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
      CrudRecord<String> crudRecord3 = CrudRecord.create(record3, CrudType.NO_CHANGED);
      CrudRecord<String> crudRecord4 = CrudRecord.create(record4, CrudType.DELETED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2, crudRecord3,
          crudRecord4);

      List<String> changedRecords = CrudRecords.of(records).changed();

      // Assertions
      assertThat(changedRecords).as("Changed records should not be null").isNotNull();
      assertThat(changedRecords.size()).as("Should have only one changed record").isEqualTo(3);
      assertThat(changedRecords).as("Available records should match input records")
          .contains(record1, record2, record4);
    }
  }
}

/*
      String record1 = "Record 1";
      String record2 = "Record 2";
      String record3 = "Record 3";
      String record4 = "Record 4";
      CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
      CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
      CrudRecord<String> crudRecord3 = CrudRecord.create(record3, CrudType.NO_CHANGED);
      CrudRecord<String> crudRecord4 = CrudRecord.create(record4, CrudType.DELETED);
      List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2, crudRecord3,
          crudRecord4);

 */
@Nested
class GetAvailableRecordsTest {

  @Test
  void shouldReturnEmptyListWhenNoRecordsAreAvailable() {
    // Initialize test data
    String record1 = "Record 1";
    String record2 = "Record 2";
    CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.DELETED);
    CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.DELETED);
    List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

    // Method call
    List<String> availableRecords = CrudRecords.of(records).getAvailableRecords();

    // Assertions
    assertThat(availableRecords).as("Available records should be empty").isEmpty();
  }

  @Test
  void shouldReturnMultipleAvailableRecords() {
    // Initialize test data
    String record1 = "Record 1";
    String record2 = "Record 2";
    CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
    CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
    List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2);

    // Method call
    List<String> availableRecords = CrudRecords.of(records).getAvailableRecords();

    // Assertions
    assertThat(availableRecords).as("Available records should not be null").isNotNull();
    assertThat(availableRecords.size()).as("Should have two available records").isEqualTo(2);
    assertThat(availableRecords).as("Available records should match input records")
        .contains(record1, record2);
  }

  @Test
  void shouldReturnMultipleAvailableRecords2() {
    // Initialize test data
    String record1 = "Record 1";
    String record2 = "Record 2";
    String record3 = "Record 3";
    String record4 = "Record 4";
    CrudRecord<String> crudRecord1 = CrudRecord.create(record1, CrudType.CREATED);
    CrudRecord<String> crudRecord2 = CrudRecord.create(record2, CrudType.UPDATED);
    CrudRecord<String> crudRecord3 = CrudRecord.create(record3, CrudType.NO_CHANGED);
    CrudRecord<String> crudRecord4 = CrudRecord.create(record4, CrudType.DELETED);
    List<CrudRecord<String>> records = List.of(crudRecord1, crudRecord2, crudRecord3,
        crudRecord4);

    // Method call
    List<String> availableRecords = CrudRecords.of(records).getAvailableRecords();

    // Assertions
    assertThat(availableRecords).as("Available records should not be null").isNotNull();
    assertThat(availableRecords.size()).as("Should have two available records").isEqualTo(3);
    assertThat(availableRecords).as("Available records should match input records")
        .contains(record1, record2, record3);
  }
}
