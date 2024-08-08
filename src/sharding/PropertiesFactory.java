/*
 * Copyright (c) @ justbk. 2021-2031. All rights reserved.
 */

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Title: the PropertiesFactory class.
 * <p>
 * Description:
 *
 * @author Administrator
 * @version [issueManager 0.0.1, 2021/10/18]
 * @since 2021/10/18
 */
public class PropertiesFactory {
    private static Logger log = Logger.getLogger(PropertiesFactory.class);
    
    private static final int INVALID_SHARDING_ID = -1;
    
    public static PropertiesFactory instance = new PropertiesFactory();
    
    public int delayTime;
    
    public Set<Integer> skipTransTypes = new HashSet<>();
    
    public int terminals;
    
    public int shardingNumber;
    public int shardingId = INVALID_SHARDING_ID;
    public int warehouseTotal;
    private int shardingWarehouseBase;
    public int shardingOffset = 0;
    
    
    public boolean useProxy = false;
    
    public boolean logTrace = false;
    
    public String connBalance;
    public List<String> conns = new ArrayList<>(10);
    public AtomicInteger connId = new AtomicInteger(0);
    
    public void init(Properties prop) {
        terminals = Integer.parseInt(prop.getProperty("terminals"));
        
        useProxy = Boolean.parseBoolean(prop.getProperty("useProxy", "false"));
        log.info("use proxy = " + useProxy);
    
        logTrace = Boolean.parseBoolean(prop.getProperty("logTrace", "false"));
        log.info("use logTrace = " + logTrace);
        
        PerfermanceFactory.instance.init();
        
        delayTime = Integer.parseInt(prop.getProperty("delayTime", "-1"));
        
        log.info("delay time = " + delayTime);
    
        shardingNumber = Integer.parseInt(prop.getProperty("shardingNumber", "1"));
        log.info("sharding number = " + shardingNumber);
    
        warehouseTotal = Integer.parseInt(prop.getProperty("warehouses"));
        
        shardingWarehouseBase = warehouseTotal / shardingNumber;
        
        shardingId = Integer.parseInt(prop.getProperty("shardingId", "0")) - 1;
        log.info("sharding id = " + shardingId);
        
        shardingOffset = shardingId * shardingWarehouseBase;
        
        connBalance = prop.getProperty("connBalance", "");
        initConnBalance();
        
        String types = prop.getProperty("skipTransType", "");
        skipTransTypes.addAll(Arrays.stream(types.split(","))
                .map(str->str.trim())
                .filter(str -> !str.isEmpty())
                .map(SkipTrans.Str2Code::str2Code)
                .map(code->code.code)
                .collect(Collectors.toSet())
        );
        log.info("=======================================");
        log.info("skiped types=" + types);
        log.info("skiped int code=" + skipTransTypes);
        
    }
    
    public void initConnBalance() {
        conns.addAll(Arrays.stream(connBalance.split(","))
                .map(str -> str.trim())
                .filter(str-> !"".equals(str))
                .map(str -> {
                    if (str.contains(":")) {
                        return str;
                    } else {
                        return str + ":" + "5432";
                    }
                }).collect(Collectors.toSet()));
        
    }
    
    public String getConnection(String conn) {
        if (conns.isEmpty()) {
            return conn;
        }
        int curId = connId.incrementAndGet() % conns.size();
        int firstPos = conn.indexOf("//");
        int nextPos = conn.indexOf('/', firstPos + "//".length());
        
        return conn.substring(0, firstPos) + "//" + conns.get(curId) + conn.substring(nextPos);
    }
    
    public int wareHouseRangeConvert(int wareHouse) {
        if (shardingId == INVALID_SHARDING_ID) {
            return wareHouse;
        }
        return wareHouse  + shardingOffset;
    }
    
    public int getWarehouseRange() {
        if (shardingId == INVALID_SHARDING_ID) {
            return warehouseTotal;
        }
        return shardingWarehouseBase;
    }
}
