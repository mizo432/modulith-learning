/**
 * プロジェクト活動管理
 */
@ApplicationModule(allowedDependencies = {
    "common :: annotation",
    "common :: verifier",
    "common :: primitive",
    "shared :: entity",
    "shared :: value",
    "shared :: date"})
package undecided.erp.projectActivity;

import org.springframework.modulith.ApplicationModule;
