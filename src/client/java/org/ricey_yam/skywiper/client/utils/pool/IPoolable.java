package org.ricey_yam.skywiper.client.utils.pool;

public interface IPoolable {
    void reset();
    void update(Object... args);
    void release();
}
