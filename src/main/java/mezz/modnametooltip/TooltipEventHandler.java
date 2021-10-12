package mezz.modnametooltip;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.apache.commons.lang3.StringUtils;

public class TooltipEventHandler {
	private final Config config;

	public TooltipEventHandler(Config config) {
		this.config = config;
	}

	public void onToolTip(ItemTooltipEvent event) {
		String modNameFormat = config.getModNameFormat();
		if (!modNameFormat.isEmpty()) {
			ItemStack itemStack = event.getItemStack();
			String modName = getModName(itemStack);
			if (modName != null) {
				var toolTip = event.getToolTip();
				if (!isModNameAlreadyPresent(toolTip, modName)) {
					toolTip.add(new TextComponent(modNameFormat + modName));
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
				return ModList.get().getModContainerById(modId)
					.map(modContainer -> modContainer.getModInfo().getDisplayName())
					.orElse(StringUtils.capitalize(modId));
			}
		}
		return null;
	}

	private static boolean isModNameAlreadyPresent(List<Component> tooltip, String modName) {
		if (tooltip.size() > 1) {
			Component line = tooltip.get(tooltip.size() - 1);
			String lineString = line.getString();
			String withoutFormatting = ChatFormatting.stripFormatting(lineString);
			return modName.equals(withoutFormatting);
		}
		return false;
	}
}
