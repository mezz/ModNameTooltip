package mezz.modnametooltip;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
			if (ForgeRegistries.ITEMS.containsValue(item)) {
				ResourceLocation itemResourceLocation = ForgeRegistries.ITEMS.getKey(item);
				String modId = itemResourceLocation.getResourceDomain();
				ModContainer modContainer = Loader.instance().getIndexedModList().get(modId);
				if (modContainer != null) {
					return modContainer.getName();
				} else if (modId.equals("minecraft")) {
					return "Minecraft";
				}
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
