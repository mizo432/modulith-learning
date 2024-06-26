@ApplicationModule(allowedDependencies = {
    "relMgmt :: actor",
    "relMgmt :: developer",
    "relMgmt :: product-owner",
    "relMgmt :: party",
    "relMgmt :: person",
    "relMgmt :: actor",
    "shared :: entity",
    "shared :: value",
    "common :: verifier",
    "common :: primitive"

})
package undecided.erp.scrum;

import org.springframework.modulith.ApplicationModule;