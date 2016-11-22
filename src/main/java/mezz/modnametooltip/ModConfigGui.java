package mezz.modnametooltip;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModConfigGui extends GuiConfig {
	public ModConfigGui(GuiScreen parent) {
		super(parent, getConfigElements(), ModNameTooltip.MODID, false, false, getTitle());
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> configElements = new ArrayList<IConfigElement>();

		Config config = ModNameTooltip.config;
		if (config != null) {
			ConfigCategory categoryFormatting = config.getConfig().getCategory(Config.CATEGORY_FORMATTING);
			configElements.addAll(new ConfigElement(categoryFormatting).getChildElements());
		}

		return configElements;
	}

	private static String getTitle() {
		Config config = ModNameTooltip.config;
		if (config != null) {
			return GuiConfig.getAbridgedConfigPath(config.getConfig().toString());
		} else {
			return ModNameTooltip.MODID;
		}
	}
}
