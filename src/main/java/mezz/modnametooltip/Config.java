package mezz.modnametooltip;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {
	public static final String CATEGORY_FORMATTING = "formatting";
	private static final String defaultModNameFormatFriendly = "blue italic";

	private final Configuration config;
	private String modNameFormat = parseFriendlyModNameFormat(defaultModNameFormatFriendly);

	public Config(FMLPreInitializationEvent event) {
		final File configFile = new File(event.getModConfigurationDirectory(), ModNameTooltip.MODID + ".cfg");
		config = new Configuration(configFile, "1.0.0");

		loadConfig();
	}

	private void loadConfig() {
		EnumSet<TextFormatting> validFormatting = EnumSet.allOf(TextFormatting.class);
		validFormatting.remove(TextFormatting.RESET);
		String[] validValues = new String[validFormatting.size()];
		int i = 0;
		for (TextFormatting formatting : validFormatting) {
			validValues[i] = formatting.getFriendlyName().toLowerCase(Locale.ENGLISH);
			i++;
		}

		Property prop = config.get(CATEGORY_FORMATTING, "modNameFormat", defaultModNameFormatFriendly);
		prop.setLanguageKey("config.modnametooltip.formatting.modNameFormat");
		prop.setComment(I18n.format("config.modnametooltip.formatting.modNameFormat.comment", Arrays.toString(validValues)));

		String modNameFormatFriendly = prop.getString();
		modNameFormat = parseFriendlyModNameFormat(modNameFormatFriendly);

		if (config.hasChanged()) {
			config.save();
		}
	}

	public Configuration getConfig() {
		return config;
	}

	public String getModNameFormat() {
		return modNameFormat;
	}

	private static String parseFriendlyModNameFormat(String formatWithEnumNames) {
		String format = "";
		if (formatWithEnumNames.isEmpty()) {
			return format;
		}
		String[] strings = formatWithEnumNames.split(" ");
		for (String string : strings) {
			TextFormatting valueByName = TextFormatting.getValueByName(string);
			if (valueByName != null) {
				format += valueByName.toString();
			} else {
				FMLLog.severe("Invalid format: " + string);
			}
		}
		return format;
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (ModNameTooltip.MODID.equals(eventArgs.getModID())) {
			loadConfig();
		}
	}
}
