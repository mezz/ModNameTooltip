package mezz.modnametooltip;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

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
				List<ITextComponent> toolTip = event.getToolTip();
				if (!isModNameAlreadyPresent(toolTip, modName)) {
					toolTip.add(new StringTextComponent(modNameFormat + modName));
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

	private static boolean isModNameAlreadyPresent(List<ITextComponent> tooltip, String modName) {
		if (tooltip.size() > 1) {
			ITextComponent line = tooltip.get(tooltip.size() - 1);
			String lineString = line.getString();
			String withoutFormatting = TextFormatting.getTextWithoutFormattingCodes(lineString);
			return modName.equals(withoutFormatting);
		}
		return false;
	}
}
