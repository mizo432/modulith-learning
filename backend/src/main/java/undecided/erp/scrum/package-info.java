@ApplicationModule(allowedDependencies = {
    "relMgmt :: actor",
    "relMgmt :: developer",
    "relMgmt :: product-owner",
    "shared :: entity",
    "shared :: value",
    "common :: precondition"

})
package undecided.erp.scrum;

import org.springframework.modulith.ApplicationModule;