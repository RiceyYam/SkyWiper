package org.ricey_yam.skywiper.client.utils.skyblock.message;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.StatusValue;

import java.util.regex.Matcher;

@Getter
@Setter
public class StatusMessage implements ISkbMessage {
    private long health;
    private long maxHealth;
    private long mana;
    private long maxMana;
    private long defense;

    public StatusMessage() {
        reset();
    }

    @Override
    public void onReceive() {
        SkbPlayerHelper.refreshPlayerStatus(this);
    }

    public void reset(){
        this.health = StatusValue.INVALID_VALUE;
        this.maxHealth = StatusValue.INVALID_VALUE;
        this.mana = StatusValue.INVALID_VALUE;
        this.maxMana = StatusValue.INVALID_VALUE;
        this.defense = StatusValue.INVALID_VALUE;
    }

    public void refreshStatusFromMatcher(Text message,Matcher statusMatcher) {
        this.health = getStatusValueSafely(statusMatcher,"health");
        this.maxHealth = getStatusValueSafely(statusMatcher,"maxHealth");
        this.mana = getStatusValueSafely(statusMatcher,"mana");
        this.maxMana = getStatusValueSafely(statusMatcher,"maxMana");
        this.defense = getStatusValueSafely(statusMatcher,"defense");
    }

    private static long getStatusValueSafely(Matcher statusMatcher,String statusName) {
        try{
            return Long.parseLong(statusMatcher.group(statusName));
        }
        catch (Exception e){
            return StatusValue.INVALID_VALUE;
        }
    }
}
