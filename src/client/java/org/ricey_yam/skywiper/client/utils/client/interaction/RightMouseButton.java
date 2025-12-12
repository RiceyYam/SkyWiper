package org.ricey_yam.skywiper.client.utils.client.interaction;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.option.KeyBinding;
import org.ricey_yam.skywiper.client.SkyWiperClient;

@Getter
@Setter
public class RightMouseButton extends Button {
    public RightMouseButton() {
        super();
    }
    public RightMouseButton(KeyBinding keyBinding) {
        super(keyBinding);
    }
}
