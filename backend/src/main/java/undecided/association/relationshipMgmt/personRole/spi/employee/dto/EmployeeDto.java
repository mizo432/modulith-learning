package undecided.association.relationshipMgmt.personRole.spi.employee.dto;

import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.association.relationshipMgmt.spi.party.contact.EmailAddress;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

public record EmployeeDto(
    SnowflakeId employeeId,
    EmployeeNo employeeNo,
    SearchName searchName,
    FullName fullName,
    Person person,
    EmailAddress emailAddress,
    PhoneNo phoneNo) {}
