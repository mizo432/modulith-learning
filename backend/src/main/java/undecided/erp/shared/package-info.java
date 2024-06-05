@ApplicationModule(allowedDependencies = {"common::application",
    "common :: snowflake",
    "common :: dateProvider",
    "common :: precondition"
})
package undecided.erp.shared;

import org.springframework.modulith.ApplicationModule;