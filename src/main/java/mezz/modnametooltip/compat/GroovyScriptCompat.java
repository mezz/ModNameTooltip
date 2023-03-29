package mezz.modnametooltip.compat;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.sandbox.RunConfig;
import mezz.modnametooltip.ModNameTooltip;
import net.minecraft.item.Item;

import java.util.Objects;

// Special Get Pack Name for Groovy, which reads straight from the RunConfig
public class GroovyScriptCompat {
    public static String getName(Item item) {
        if (ModNameTooltip.isGroovyLoaded()){
            RunConfig runConfig = GroovyScript.getRunConfig();
            if (Objects.requireNonNull(item.getRegistryName()).getResourceDomain().equals(runConfig.getPackId()))
                return runConfig.getPackName();
        }
        return null;
    }
}
