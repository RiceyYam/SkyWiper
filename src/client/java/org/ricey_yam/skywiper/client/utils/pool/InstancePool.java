package org.ricey_yam.skywiper.client.utils.pool;

import lombok.Getter;

import javax.swing.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Getter
public class InstancePool<T extends IPoolable>{
    private final LinkedBlockingQueue<T> pool = new LinkedBlockingQueue<>();
    private final Class<T> tClass;
    private final int poolSize;

    public InstancePool(int poolSize,Class<T> tClass){
        this.tClass =  tClass;
        this.poolSize = poolSize;
        initPool();
    }

    public void initPool(){
        try{
            for (int i = 0; i < poolSize; i++) {
                pool.add(tClass.getDeclaredConstructor().newInstance());
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("初始化对象池发生错误：" + e.getMessage());
        }
    }

    public T borrowInstance(Object... args){
        if(pool.isEmpty()) initPool();
        try {
            var instance = pool.poll(1000, TimeUnit.MILLISECONDS);
            if (instance != null) {
                instance.update(args);
                return instance;
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public void returnInstance(T instance){
        if (instance == null) return;
        instance.reset();
        pool.offer(instance);
    }
}
