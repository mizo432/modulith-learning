@ApplicationModule(allowedDependencies = {
    "relationship :: actor",
    "relationship :: developer",
    "relationship :: product-owner",
    "relationship :: party",
    "relationship :: person",
    "relationship :: actor",
    "shared :: entity",
    "shared :: value",
    "common :: verifier",
    "common :: primitive"

})
package undecided.erp.scrum;

import org.springframework.modulith.ApplicationModule;