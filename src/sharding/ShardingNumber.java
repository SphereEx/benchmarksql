/*
 * Copyright (c) @ justbk. 2021-2031. All rights reserved.
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Title: the ShardingNumber class.
 * <p>
 * Description:
 *
 * @author Administrator
 * @version [issueManager 0.0.1, 2021/10/18]
 * @since 2021/10/18
 */
public class ShardingNumber {
    
    /**
     * To convert rndWarehouse match to warehouse.
     * @param warehouse the base warehouse
     * @param rndWarehouse the random warehouse, may be not match with warehouse
     * @return the matched random warehouse
     */
    public static int oneSharding(int warehouse, int rndWarehouse) {
        int shardingNumber = PropertiesFactory.instance.shardingNumber;
        int sub = Math.abs(rndWarehouse - warehouse) % shardingNumber;
        if (sub == 0) {
            return rndWarehouse;
        }
        sub = rndWarehouse > warehouse ? sub : -sub;
        if (rndWarehouse - sub > 0) {
            return rndWarehouse - sub;
        } else {
            return rndWarehouse + shardingNumber - sub;
        }
    }
    
    public static long MOD;
    public static Map<Long, Integer> thread2Mod = new ConcurrentHashMap<>();
    public static AtomicInteger modId = new AtomicInteger(0);
    
    public static void init(long itemRndBase) {
        long itemCount = 100000 - itemRndBase;
        MOD = itemCount / (PropertiesFactory.instance.terminals);
    }
    
    public static int modId() {
        long tId = threadId();
        return thread2Mod.get(tId);
    }
    
    public static void threadInit() {
        thread2Mod.put(threadId(), modId.getAndIncrement());
    }
    
    public static long threadId() {
        return Thread.currentThread().getId();
    }
}
