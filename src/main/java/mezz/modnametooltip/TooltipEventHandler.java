package mezz.modnametooltip;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.text.WordUtils;

public class TooltipEventHandler {
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onToolTip(ItemTooltipEvent event) {
		String modNameFormat = getModNameFormat();
		if (!modNameFormat.isEmpty()) {
			ItemStack itemStack = event.getItemStack();
			String modName = getModName(itemStack);
			if (modName != null) {
				List<String> toolTip = event.getToolTip();
				if (!isModNameAlreadyPresent(toolTip, modName)) {
					toolTip.add(modNameFormat + modName);
				}
			}
		}
	}

	@Nullable
	private static String getModName(ItemStack itemStack) {
		if (!itemStack.isEmpty()) {
			Item item = itemStack.getItem();

			String modId = item.getCreatorModId(itemStack);
			if (modId != null) {
				Map<String, ModContainer> indexedModList = Loader.instance().getIndexedModList();
				ModContainer modContainer = indexedModList.get(modId);
				if (modContainer != null) {
					return modContainer.getName();
				}
			}

			if (item.getRegistryName() != null) {
				return WordUtils.capitalize(item.getRegistryName().getResourceDomain());
			}
		}
		return null;
	}

	private static String getModNameFormat() {
		Config config = ModNameTooltip.config;
		if (config != null) {
			return config.getModNameFormat();
		}
		return "";
	}

	private static boolean isModNameAlreadyPresent(List<String> tooltip, String modName) {
		if (tooltip.size() > 1) {
			String lastTooltipLine = tooltip.get(tooltip.size() - 1);
			lastTooltipLine = TextFormatting.getTextWithoutFormattingCodes(lastTooltipLine);
			if (modName.equals(lastTooltipLine)) {
				return true;
			}
		}
		return false;
	}
}
