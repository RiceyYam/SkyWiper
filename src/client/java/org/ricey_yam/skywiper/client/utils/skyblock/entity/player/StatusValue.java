package org.ricey_yam.skywiper.client.utils.skyblock.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.utils.pool.IPoolable;
import org.ricey_yam.skywiper.client.utils.pool.InstancePool;

@Getter
@Setter
public class StatusValue implements IPoolable {
    @Getter
    private static final InstancePool<StatusValue> instancePool = new InstancePool<>(50,StatusValue.class);
    public static final long INVALID_VALUE = -987654321;
    private long value;

    public StatusValue() {
        reset();
    }

    @Override
    public void reset(){
        this.value = INVALID_VALUE;
    }

    @Override
    public void update(Object... args) {
        this.value = args.length == 0 ? INVALID_VALUE : (long)args[0];
    }

    @Override
    public void release() {
        instancePool.returnInstance(this);
    }

    public static StatusValue getNewStatusValue(StatusValue origStatusValue, long newValue) {
        if (newValue != StatusValue.INVALID_VALUE) {
            if (origStatusValue != null) {
                origStatusValue.release();
            }
            var newStatusValue = instancePool.borrowInstance(newValue);
            if (newStatusValue != null) {
                return newStatusValue;
            }
        }
        return origStatusValue;
    }
}
