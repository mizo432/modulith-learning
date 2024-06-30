/**
 * 関係管理
 */
@ApplicationModule(allowedDependencies = {
    "common :: annotation",
    "shared :: entity",
    "shared :: value",
    "shared :: date"})
package undecided.erp.relationship;

import org.springframework.modulith.ApplicationModule;