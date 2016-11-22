package mezz.modnametooltip;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TooltipEventHandler {
	@Nullable
	public String getModNameForItem(Item item) {
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
		return null;
	}

	@SubscribeEvent
	public void onToolTip(ItemTooltipEvent event) {
		Config config = ModNameTooltip.config;
		if (config != null) {
			String modNameFormat = config.getModNameFormat();
			if (!modNameFormat.isEmpty()) {
				ItemStack itemStack = event.getItemStack();
				if (!itemStack.isEmpty()) {
					String modName = getModNameForItem(itemStack.getItem());
					if (modName != null) {
						event.getToolTip().add(modNameFormat + modName);
					}
				}
			}
		}
	}
}
