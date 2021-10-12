package mezz.modnametooltip;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ModNameTooltip {
	public ModNameTooltip() {
		DistExecutor.safeRunWhenOn(Dist.CLIENT, ()-> ModNameTooltipClient::run);
	}
}
