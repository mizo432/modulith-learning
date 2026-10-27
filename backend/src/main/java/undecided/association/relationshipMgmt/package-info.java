@ApplicationModule(
    id = "relationshipMgmt",
    displayName = "関係管理サブドメイン",
    allowedDependencies = {"agiloom", "shared", "supporting :: snowflake"})
@NamedInterface(propagate = true)
package undecided.association.relationshipMgmt;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.NamedInterface;
