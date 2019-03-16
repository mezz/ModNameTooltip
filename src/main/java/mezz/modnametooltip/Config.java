package mezz.modnametooltip;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Locale;
import java.util.stream.Collectors;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String CATEGORY_FORMATTING = "formatting";

	private final ForgeConfigSpec config;
	private final ConfigValue<String> modNameFormatFriendly;
	@Nullable
	private String cachedModNameFormat;

	public Config() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		EnumSet<TextFormatting> validFormatting = EnumSet.allOf(TextFormatting.class);
		validFormatting.remove(TextFormatting.RESET);
		String validValues = validFormatting.stream()
			.map(formatting -> formatting.getFriendlyName().toLowerCase(Locale.ENGLISH))
			.collect(Collectors.joining(", "));

		builder.push(CATEGORY_FORMATTING);
		modNameFormatFriendly = builder
			.comment("How the mod name should be formatted in the tooltip. Leave blank to disable. Valid values:", validValues)
			.translation("config.modnametooltip.formatting.modNameFormat")
			.define("modNameFormat", "blue italic");

		config = builder.build();
	}

	public ForgeConfigSpec getConfigSpec() {
		return config;
	}

	public String getModNameFormat() {
		if (cachedModNameFormat == null) {
			cachedModNameFormat = parseFriendlyModNameFormat(modNameFormatFriendly.get());
		}
		return cachedModNameFormat;
	}

	private static String parseFriendlyModNameFormat(String formatWithEnumNames) {
		if (formatWithEnumNames.isEmpty()) {
			return "";
		}
		StringBuilder format = new StringBuilder();
		String[] strings = formatWithEnumNames.split(" ");
		for (String string : strings) {
			TextFormatting valueByName = TextFormatting.getValueByName(string);
			if (valueByName != null) {
				format.append(valueByName.toString());
			} else {
				LOGGER.error("Invalid format: " + string);
			}
		}
		return format.toString();
	}

	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (ModNameTooltip.MOD_ID.equals(eventArgs.getModID())) {
			cachedModNameFormat = null;
		}
	}
}
