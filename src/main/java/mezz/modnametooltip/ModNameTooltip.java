package mezz.modnametooltip;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class ModNameTooltip {
	public ModNameTooltip() {
		if (FMLEnvironment.dist == Dist.CLIENT) ModNameTooltipClient.run();
	}
}
