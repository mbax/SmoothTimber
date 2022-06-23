package com.syntaxphoenix.spigot.smoothtimber.version.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.syntaxphoenix.spigot.smoothtimber.config.Message;
import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;
import com.syntaxphoenix.spigot.smoothtimber.version.changer.*;
import com.syntaxphoenix.syntaxapi.utils.java.Strings;

public class VersionExchanger {

    private static MCVersion CORE_VERSION;
    private static VersionChanger VERSION_CHANGER;

    public static String getMinecraftVersion() {
        return Bukkit.getVersion().split(" ")[2].replace(")", "");
    }

    public static MCVersion getCoreVersion() {
        if (CORE_VERSION == null) {
            return CORE_VERSION = MCVersion.fromString(getMinecraftVersion());
        }
        return CORE_VERSION;
    }

    private static VersionChanger initVersionChanger(MCVersion core) {
        switch (core) {
        case v1_8x:
            return new v1_8xChanger();
        case v1_9x:
            return new v1_9xChanger();
        case v1_11x:
            return new v1_11xChanger();
        case v1_13x:
            return new v1_13xChanger();
        case v1_16x:
            return new v1_16xChanger();
        case v1_19x:
            return new v1_19xChanger();
        default: {
            return null;
        }
        }
    }

    public static VersionChanger getVersionChanger(String minecraft) {
        if (VERSION_CHANGER != null) {
            return VERSION_CHANGER;
        }
        MCVersion core = MCVersion.fromString(minecraft);
        VersionChanger changer = initVersionChanger(core);
        if (changer != null) {
            PluginUtils.sendConsoleMessage(false,
                Message.GLOBAL_PREFIX.colored() + ' ' + Message.STARTUP_VERSION_SUPPORTED.colored(new String[][] {
                    {
                        "%minecraft%",
                        minecraft
                    },
                    {
                        "%core%",
                        core.name()
                    }
                }));
            return VERSION_CHANGER = changer;
        }
        PluginUtils.sendConsoleMessage(false,
            Message.GLOBAL_PREFIX.colored() + ' ' + Message.STARTUP_VERSION_UNSUPPORTED.colored(new String[] {
                "%minecraft%",
                minecraft
            }));
        PluginUtils.sendConsoleMessage(false, Message.GLOBAL_PREFIX.colored() + ' ' + Message.STARTUP_VERSION_NEED222UPDATE.colored());
        PluginUtils.sendConsoleMessage(false,
            Message.GLOBAL_PREFIX.colored() + ' ' + Message.STARTUP_VERSION_VERSIONS.colored(new String[] {
                "%versions%",
                Strings.toString(MCVersion.getSupportedVersions(), Message.GLOBAL_LIST222SPLIT.message())
            }));
        Bukkit.getPluginManager().disablePlugin(PluginUtils.MAIN);
        return VERSION_CHANGER = new NOPChanger();
    }

    public static boolean checkPermission(WoodType type, Player play) {
        return play.hasPermission("smoothtimber.*") || play.hasPermission("smoothtimber." + type.name().toLowerCase());
    }

    public static boolean checkPermission(String permission, Player play) {
        return play.hasPermission("smoothtimber.*") || play.hasPermission("smoothtimber." + permission);
    }

}
