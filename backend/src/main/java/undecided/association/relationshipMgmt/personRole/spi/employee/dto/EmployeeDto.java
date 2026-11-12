package undecided.association.relationshipMgmt.personRole.spi.employee.dto;

import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.supporting.snowflake.SnowflakeId;

public record EmployeeDto(
    SnowflakeId employeeId, EmployeeNo employeeNo, SearchName searchName, FullName fullName) {}
