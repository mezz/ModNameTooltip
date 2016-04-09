package mezz.modnametooltip;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
		modid = ModNameTooltip.MODID,
		version = ModNameTooltip.VERSION,
		acceptedMinecraftVersions = "[1.9,1.10)",
		dependencies = "required-after:Forge@[12.16.0.1819,);",
		clientSideOnly = true
)
public class ModNameTooltip {
	public static final String MODID = "ModNameTooltip";
	public static final String VERSION = "@VERSION@";

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (Loader.isModLoaded("Waila")) {
			FMLLog.warning("Waila detected. It also adds the Mod Name to the Tooltip. Deactivating " + MODID + '.');
			return;
		}
		TooltipEventHandler tooltipEventHandler = new TooltipEventHandler();
		MinecraftForge.EVENT_BUS.register(tooltipEventHandler);
	}
}
