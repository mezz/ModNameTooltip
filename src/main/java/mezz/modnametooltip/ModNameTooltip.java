package mezz.modnametooltip;

import javax.annotation.Nullable;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = ModNameTooltip.MODID,
		name = "Mod Name Tooltip",
		version = ModNameTooltip.VERSION,
		acceptedMinecraftVersions = "[1.12.2,)",
		dependencies = "required-after:forge@[14.23.0.2500,);",
		guiFactory = "mezz.modnametooltip.ConfigGuiFactory",
		clientSideOnly = true
)
public class ModNameTooltip {
	public static final String MODID = "modnametooltip";
	public static final String VERSION = "@VERSION@";

	@Nullable
	public static Config config;

	private static boolean isGroovyLoaded = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Config(event);
		MinecraftForge.EVENT_BUS.register(config);
		isGroovyLoaded = Loader.isModLoaded("groovyscript");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TooltipEventHandler tooltipEventHandler = new TooltipEventHandler();
		MinecraftForge.EVENT_BUS.register(tooltipEventHandler);
	}

	public static boolean isGroovyLoaded(){
		return isGroovyLoaded;
	}
}
