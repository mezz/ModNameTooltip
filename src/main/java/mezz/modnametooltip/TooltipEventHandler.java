package mezz.modnametooltip;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TooltipEventHandler {
	private static final String chatFormatting = TextFormatting.BLUE.toString() + TextFormatting.ITALIC.toString();

	private final Map<String, String> modNamesForIds = new HashMap<String, String>();

	public TooltipEventHandler() {
		Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
		for (Map.Entry<String, ModContainer> modEntry : modMap.entrySet()) {
			String lowercaseId = modEntry.getKey().toLowerCase(Locale.ENGLISH);
			String modName = modEntry.getValue().getName();
			modNamesForIds.put(lowercaseId, modName);
		}
	}

	@Nonnull
	public String getModNameForItem(@Nonnull Item item) {
		ResourceLocation itemResourceLocation = (ResourceLocation) GameData.getItemRegistry().getNameForObject(item);
		String modId = itemResourceLocation.getResourceDomain();
		String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);
		String modName = modNamesForIds.get(lowercaseModId);
		if (modName == null) {
			modName = WordUtils.capitalize(modId);
			modNamesForIds.put(lowercaseModId, modName);
		}
		return modName;
	}

	@SubscribeEvent
	public void onToolTip(@Nonnull ItemTooltipEvent event) {
		ItemStack itemStack = event.itemStack;
		if (itemStack == null) {
			return;
		}

		Item item = itemStack.getItem();
		if (item == null) {
			return;
		}

		String modName = getModNameForItem(item);
		event.toolTip.add(chatFormatting + modName);
	}
}
