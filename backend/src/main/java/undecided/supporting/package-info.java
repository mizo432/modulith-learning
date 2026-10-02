@ApplicationModule(
    type = ApplicationModule.Type.OPEN,
    allowedDependencies = {"relationshipMgmt", "cashSaleMgmt", "productSaleMgmt"},
    displayName = "支援サブドメイン")
/** ,支援サブドメイン */
package undecided.supporting;

import org.springframework.modulith.ApplicationModule;
