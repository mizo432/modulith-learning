@ApplicationModule(allowedDependencies = {"common::application",
    "common :: snowflake",
    "common :: dateProvider",
    "common :: verifier"
})
package undecided.erp.shared;

import org.springframework.modulith.ApplicationModule;