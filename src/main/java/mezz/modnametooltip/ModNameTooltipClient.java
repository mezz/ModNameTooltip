package mezz.modnametooltip;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.function.Consumer;

public class ModNameTooltipClient {
	public static void run() {
		Config config = new Config();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, config.getConfigSpec());
		TooltipEventHandler tooltipEventHandler = new TooltipEventHandler(config);

		IEventBus eventBus = MinecraftForge.EVENT_BUS;
		addListener(eventBus, ModConfigEvent.class, EventPriority.NORMAL, config::onConfigChanged);
		addListener(eventBus, ItemTooltipEvent.class, EventPriority.LOW, tooltipEventHandler::onToolTip);
	}

	private static <T extends Event> void addListener(IEventBus eventBus, Class<T> eventType, EventPriority priority, Consumer<T> listener) {
		eventBus.addListener(priority, false, eventType, listener);
	}
}
