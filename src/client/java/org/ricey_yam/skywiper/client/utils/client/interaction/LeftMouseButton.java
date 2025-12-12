package org.ricey_yam.skywiper.client.utils.client.interaction;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Hand;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

public class LeftMouseButton extends Button {
    public LeftMouseButton() {
        super();
    }
    public LeftMouseButton(KeyBinding keyBinding) {
        super(keyBinding);
    }
    @Override
    protected void down() {
        super.down();
        var mc = ClientUtils.getClient();
        if(mc == null) return;
        var player = ClientUtils.getPlayer();
        if(player == null) return;
        player.swingHand(Hand.MAIN_HAND);
        if(mc.targetedEntity != null){
            var interactionManager = mc.interactionManager;
            if(interactionManager == null) return;
            mc.interactionManager.attackEntity(player, mc.targetedEntity);
        }
    }
}
