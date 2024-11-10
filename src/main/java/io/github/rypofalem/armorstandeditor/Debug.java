package io.github.rypofalem.armorstandeditor;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.logging.Level;

@UtilityClass
public class Debug {

    private static final boolean DEBUGS_ENABLED = true;

    public static void log(String msg) {
        if (!DEBUGS_ENABLED)
            return;

        //ArmorStandEditorPlugin.instance().debugMsgHandler(msg);
        Bukkit.getLogger().log(Level.INFO, msg);
    }
}
