package mezz.modnametooltip;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.function.Consumer;

@Mod("modnametooltip")
public class ModNameTooltipClient {
	public ModNameTooltipClient(IEventBus modBus) {
		Config config = new Config();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, config.getConfigSpec());
		TooltipEventHandler tooltipEventHandler = new TooltipEventHandler(config);
		IEventBus eventBus = NeoForge.EVENT_BUS;
		addListener(modBus, ModConfigEvent.class, EventPriority.NORMAL, config::onConfigChanged);
		addListener(eventBus, ItemTooltipEvent.class, EventPriority.LOW, tooltipEventHandler::onToolTip);
	}
	private static <T extends Event> void addListener(IEventBus eventBus, Class<T> eventType, EventPriority priority, Consumer<T> listener) {
		eventBus.addListener(priority, false, eventType, listener);
	}
}
