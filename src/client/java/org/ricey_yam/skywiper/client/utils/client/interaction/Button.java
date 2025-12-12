package org.ricey_yam.skywiper.client.utils.client.interaction;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.option.KeyBinding;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

@Getter
@Setter
public class Button{
    protected final KeyBinding keyBinding;
    protected int tickTimer;
    protected int releaseTickDelay;
    protected Button(){
        this.keyBinding = null;
    }
    public Button(KeyBinding keyBinding){
        this.keyBinding = keyBinding;
    }

    public void spawnRandomReleaseTickDelay(){
        releaseTickDelay = SkyWiperClient.getRandom().nextInt(5) + 1;
    }

    public void update(){
        if(!isPressed()) return;

        tickTimer++;
        if(tickTimer >= releaseTickDelay){
            up();
        }
    }

    public void click(){
        if(isPressed()) {
            up();
            return;
        }
        down();
        spawnRandomReleaseTickDelay();
    }

    protected void down(){
        tickTimer = 0;
        System.out.println("点击鼠标");
        if (keyBinding != null) {
            keyBinding.setPressed(true);
        }
        else{
            System.out.println("未绑定按键");
        }
    }

    protected void up(){
        tickTimer = 0;
        if (keyBinding != null) {
            keyBinding.setPressed(false);
        }
    }

    public boolean isEmpty(){
        return keyBinding == null;
    }

    public boolean isPressed(){
        return keyBinding != null && keyBinding.isPressed();
    }
}
