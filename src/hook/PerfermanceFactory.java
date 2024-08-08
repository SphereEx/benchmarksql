/*
 * Copyright (c) @ justbk. 2021-2031. All rights reserved.
 */

import org.apache.log4j.Logger;
import org.justbk.perf.common.AbstracePerfFactory;
import org.justbk.perf.common.conf.PerfConfig;
import org.justbk.perf.common.inf.impl.SqlPerfFactory;
import org.justbk.perf.common.logger.LogAdapt;
import org.justbk.perf.common.logger.LogProxyFactory;

/**
 * Title: the PerfmanceFactory class.
 * <p>
 * Description:
 *
 * @author Administrator
 * @version [issueManager 0.0.1, 2021/10/26]
 * @since 2021/10/26
 */
public class PerfermanceFactory {
    private PerfermanceFactory() {}
    public static PerfermanceFactory instance = new PerfermanceFactory();
    public AbstracePerfFactory perfFactory = new SqlPerfFactory();
    public LogAdapt log = LogProxyFactory.logProxy(Logger.getLogger("perf"));
    public LogAdapt logTrace = LogProxyFactory.logProxy(Logger.getLogger("perf_trace"));
    
    public void init() {
        PerfConfig.useProxy = PropertiesFactory.instance.useProxy;
        PerfConfig.logTrace = PropertiesFactory.instance.logTrace;
        perfFactory.setLog(log);
        perfFactory.setLogTrace(logTrace);
        perfFactory.init();
    }
    
    public void threadInit(Thread thread) {
        ShardingNumber.threadInit();
        perfFactory.threadInit(thread);
    }
}
