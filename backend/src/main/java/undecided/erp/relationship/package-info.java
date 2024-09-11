/**
 * 関係管理
 */
@ApplicationModule(allowedDependencies = {
    "common :: annotation",
    "common :: verifier",
    "common :: primitive",
    "shared :: entity",
    "shared :: value",
    "shared :: date"})
package undecided.erp.relationship;

import org.springframework.modulith.ApplicationModule;