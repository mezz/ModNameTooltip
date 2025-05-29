package mezz.modnametooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class TooltipEventHandler {
	private final Config config;

	public TooltipEventHandler(Config config) {
		this.config = config;
	}

	public void onToolTip(ItemTooltipEvent event) {
		String modNameFormat = config.getModNameFormat();
		if (modNameFormat.isEmpty()) {
			return;
		}
		ItemStack itemStack = event.getItemStack();
		getModName(itemStack)
			.ifPresent(modName -> {
				var toolTip = event.getToolTip();
				if (!isModNameAlreadyPresent(toolTip, modName)) {
					toolTip.add(Component.literal(modNameFormat + modName));
				}
			});
	}

	private static Optional<String> getModName(ItemStack itemStack) {
		return getCreatorModId(itemStack)
			.map(TooltipEventHandler::getModName);
	}

	private static Optional<String> getCreatorModId(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return Optional.empty();
		}
		Item item = itemStack.getItem();
		return Optional.ofNullable(Minecraft.getInstance())
			.flatMap(minecraft -> Optional.ofNullable(minecraft.level))
			.map(Level::registryAccess)
			.map(registryAccess -> item.getCreatorModId(registryAccess, itemStack));
	}

	private static String getModName(String modId) {
		ModList modList = ModList.get();
		return modList.getModContainerById(modId)
			.map(ModContainer::getModInfo)
			.map(IModInfo::getDisplayName)
			.orElseGet(() -> StringUtils.capitalize(modId));
	}

	private static boolean isModNameAlreadyPresent(List<Component> tooltip, String modName) {
		if (tooltip.size() > 1) {
			Component line = tooltip.getLast();
			String lineString = line.getString();
			String withoutFormatting = ChatFormatting.stripFormatting(lineString);
			return modName.equals(withoutFormatting);
		}
		return false;
	}
}
