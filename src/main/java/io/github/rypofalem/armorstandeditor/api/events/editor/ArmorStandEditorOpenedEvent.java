package io.github.rypofalem.armorstandeditor.api.events.editor;

import io.github.rypofalem.armorstandeditor.menu.EditorMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@SuppressWarnings("LombokGetterMayBeUsed")
public class ArmorStandEditorOpenedEvent extends EditorMenuEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled = false;

    public ArmorStandEditorOpenedEvent(Player player, EditorMenu menu) {
        super(player, menu);
    }
}
