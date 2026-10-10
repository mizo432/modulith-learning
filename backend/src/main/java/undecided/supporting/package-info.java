@ApplicationModule(
    type = Type.OPEN,
    allowedDependencies = {
      "generic.greeting",
      "relationshipMgmt.party",
      "relationshipMgmt.orgRole",
      "relationshipMgmt.personRole",
      "relationshipMgmt.partyRole",
      "shared",
      "shared :: entity"
    })
/** ,支援サブドメイン */
package undecided.supporting;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
