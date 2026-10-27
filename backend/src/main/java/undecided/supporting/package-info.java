@ApplicationModule(
    type = Type.OPEN,
    allowedDependencies = {"generic", "relationshipMgmt", "shared", "shared :: entity", "agiloom"},
    id = "supporting",
    displayName = "支援サブドメイン")
/** ,支援サブドメイン */
package undecided.supporting;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
