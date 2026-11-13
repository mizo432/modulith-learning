package undecided.association.relationshipMgmt.personRole.spi.employee.dto;

import org.jspecify.annotations.NonNull;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.association.relationshipMgmt.spi.party.contact.EmailAddress;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

public record EmployeeDto(
    @NonNull SnowflakeId employeeId,
    @NonNull EmployeeNo employeeNo,
    @NonNull SearchName searchName,
    @NonNull FullName fullName,
    @NonNull Person person,
    @NonNull EmailAddress emailAddress,
    @NonNull PhoneNo phoneNo) {}
