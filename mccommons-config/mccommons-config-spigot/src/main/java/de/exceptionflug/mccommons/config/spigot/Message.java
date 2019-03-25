package de.exceptionflug.mccommons.config.spigot;

import de.exceptionflug.mccommons.config.shared.ConfigWrapper;
import de.exceptionflug.mccommons.core.Providers;
import de.exceptionflug.mccommons.core.providers.LocaleProvider;
import de.exceptionflug.mccommons.core.utils.FormatUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public final class Message {

    public static void send(final Player p, final ConfigWrapper config, final String messageKey, final String defaultMessage, final String... replacements) {
        send(p, config, true, messageKey, defaultMessage, replacements);
    }

    public static void send(final Player p, final ConfigWrapper config, final boolean prefix, final String messageKey, final String defaultMessage, final String... replacements) {
        p.sendMessage(getMessage(config, Providers.get(LocaleProvider.class).provide(p.getUniqueId()), prefix, messageKey, defaultMessage, replacements));
    }

    public static void broadcast(final List<Player> players, final ConfigWrapper config, final boolean prefix, final String messageKey, final String defaultMessage, final String... replacements) {
        for(final Player player : players) {
            send(player, config, prefix, messageKey, defaultMessage, replacements);
        }
    }

    public static void broadcast(final List<Player> players, final ConfigWrapper config, final String messageKey, final String defaultMessage, final String... replacements) {
        for(final Player player : players) {
            send(player, config, messageKey, defaultMessage, replacements);
        }
    }

    public static String getPrefix(final ConfigWrapper config, final Locale locale) {
        return config.getOrSetDefault("Messages.prefix."+locale.getLanguage(), "[Prefix] ");
    }

    public static String getMessage(final ConfigWrapper config, final Locale locale, final String messageKey, final String defaultMessage, final String... replacements) {
        return getMessage(config, locale, true, messageKey, defaultMessage, replacements);
    }

    public static String getMessage(final ConfigWrapper config, final Locale locale, final boolean prefix, final String messageKey, final String defaultMessage, final String... replacements) {
        if(config.isSet(messageKey+"."+locale.getLanguage())) {
            return getString0(config, locale, prefix, messageKey, defaultMessage, replacements);
        } else if(!locale.getLanguage().equalsIgnoreCase(Providers.get(LocaleProvider.class).getFallbackLocale().getLanguage())) {
            return getMessage(config, Providers.get(LocaleProvider.class).getFallbackLocale(), messageKey, defaultMessage, replacements);
        } else {
            return getString0(config, locale, prefix, messageKey, defaultMessage, replacements);
        }
    }

    private static String getString0(final ConfigWrapper config, final Locale locale, final boolean prefix, final String messageKey, final String defaultMessage, final String[] replacements) {
        String message = config.getOrSetDefault(messageKey+"."+locale.getLanguage(), defaultMessage);
        message = FormatUtils.format(message, replacements);
        if(prefix)
            message = getPrefix(config, locale)+message;
        return message;
    }

}