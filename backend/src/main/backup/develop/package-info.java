@ApplicationModule(allowedDependencies = {
    "relMgmt :: actor",
    "relMgmt :: developer",
    "shared :: entity",
    "shared :: value",
    "common :: verifier"
})
package undecided.erp.develop;

import org.springframework.modulith.ApplicationModule;