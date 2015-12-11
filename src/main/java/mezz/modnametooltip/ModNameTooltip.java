package mezz.modnametooltip;

import javax.annotation.Nonnull;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
		modid = ModNameTooltip.MODID,
		version = ModNameTooltip.VERSION,
		acceptedMinecraftVersions = "[1.8,1.8.9]",
		clientSideOnly = true,
		canBeDeactivated = true
)
public class ModNameTooltip {
	public static final String MODID = "ModNameTooltip";
	public static final String VERSION = "1.0";

	private TooltipEventHandler tooltipEventHandler;

	@NetworkCheckHandler
	public boolean checkModLists(Map<String, String> modList, Side side) {
		return !modList.containsKey("Waila");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		tooltipEventHandler = new TooltipEventHandler();
		MinecraftForge.EVENT_BUS.register(tooltipEventHandler);
	}

	@SubscribeEvent
	public void onDeactivate(@Nonnull FMLModDisabledEvent event) {
		if (tooltipEventHandler != null) {
			MinecraftForge.EVENT_BUS.unregister(tooltipEventHandler);
		}
	}
}
