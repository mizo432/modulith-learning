@ApplicationModule(
    type = Type.CLOSED,
    allowedDependencies = {"shared", "shared :: entity"})
@NamedInterface(value = "databaseDef")
package undecided.supporting.databaseDef;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
import org.springframework.modulith.NamedInterface;
