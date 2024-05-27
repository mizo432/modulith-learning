package undecided.erp.relMgmt;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmployeeDto(
    @JsonProperty("employeeId")
    Long id,

    String employeeNo,

    String firstName,

    String lastName) {

}
