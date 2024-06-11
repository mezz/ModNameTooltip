package mezz.modnametooltip;

import net.minecraft.ChatFormatting;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Locale;
import java.util.StringJoiner;

public class Config {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String CATEGORY_FORMATTING = "formatting";

	private final ModConfigSpec config;
	private final ModConfigSpec.ConfigValue<String> modNameFormatFriendly;
	@Nullable
	private String cachedModNameFormat;

	public Config() {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

		EnumSet<ChatFormatting> validFormatting = EnumSet.allOf(ChatFormatting.class);
		validFormatting.remove(ChatFormatting.RESET);

		StringJoiner validColorsJoiner = new StringJoiner(", ");
		StringJoiner validFormatsJoiner = new StringJoiner(", ");

		for (ChatFormatting chatFormatting : validFormatting) {
			String lowerCaseName = chatFormatting.getName().toLowerCase(Locale.ENGLISH);
			if (chatFormatting.isColor()) {
				validColorsJoiner.add(lowerCaseName);
			} else if (chatFormatting.isFormat()) {
				validFormatsJoiner.add(lowerCaseName);
			}
		}
		String validColors = validColorsJoiner.toString();
		String validFormats = validFormatsJoiner.toString();

		builder.push(CATEGORY_FORMATTING);
		modNameFormatFriendly = builder
			.comment("How the mod name should be formatted in the tooltip. Leave blank to disable.",
					"Use these formatting colors:", validColors,
					"With these formatting options:", validFormats
			)
			.translation("config.modnametooltip.formatting.modNameFormat")
			.define("modNameFormat", "blue italic");

		config = builder.build();
	}

	public ModConfigSpec getConfigSpec() {
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
			ChatFormatting valueByName = ChatFormatting.getByName(string);
			if (valueByName != null) {
				format.append(valueByName);
			} else {
				LOGGER.error("Invalid format: " + string);
			}
		}
		return format.toString();
	}

	public void onConfigChanged(ModConfigEvent event) {
		if ("modnametooltip".equals(event.getConfig().getModId())) {
			cachedModNameFormat = null;
		}
	}
}
