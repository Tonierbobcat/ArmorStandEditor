package io.github.rypofalem.armorstandeditor.api.events.editor;

import io.github.rypofalem.armorstandeditor.menu.EditorMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EditorMenuEvent extends Event {


    @Getter
    private final Player player;
    @Getter
    private final EditorMenu menu;

    private static final HandlerList HANDLERS = new HandlerList();

    public EditorMenuEvent(Player player, EditorMenu menu){
        this.player = player;
        this.menu = menu;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
