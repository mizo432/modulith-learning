@ApplicationModule(allowedDependencies = {"common::application",
    "common :: annotation",
    "common :: snowflake",
    "common :: dateProvider",
    "common :: verifier",
    "common :: primitive"
})
package undecided.erp.shared;

import org.springframework.modulith.ApplicationModule;