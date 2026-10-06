@ApplicationModule(
    id = "relationshipMgmt.party",
    allowedDependencies = {
      "relationshipMgmt.partyRole",
      "relationshipMgmt.personRole",
      "relationshipMgmt.orgRole"
    })
package undecided.generic.relationshipMgmt.party;

import org.springframework.modulith.ApplicationModule;
