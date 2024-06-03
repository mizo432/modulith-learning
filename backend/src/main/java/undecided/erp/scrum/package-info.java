@ApplicationModule(allowedDependencies = {
    "relMgmt :: actor",
    "relMgmt :: developer",
    "relMgmt :: product-owner",
    "shared :: entity"
})
package undecided.erp.scrum;

import org.springframework.modulith.ApplicationModule;