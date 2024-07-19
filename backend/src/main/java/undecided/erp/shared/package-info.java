@ApplicationModule(allowedDependencies = {"common::application",
    "common :: annotation",
    "common :: snowflake",
    "common :: dateProvider",
    "common :: verifier",
    "common :: primitive",
    "shared :: entity"
})
package undecided.erp.shared;

import org.springframework.modulith.ApplicationModule;