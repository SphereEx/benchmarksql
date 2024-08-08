/*
 * Copyright (c) @ justbk. 2021-2031. All rights reserved.
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Title: the RunConfigSet class.
 * <p>
 * Description:
 *
 * @author Administrator
 * @version [issueManager 0.0.1, 2021/10/19]
 * @since 2021/10/19
 */
public class RunConfigSet {
    private static final String ROOT_PATH = "D:\\zhoubin\\code\\github\\benchmarksql\\run";
    private static final String RUN_CONFIG = "props.proxy";
    private static final String EXEC_JDBC_FILE = "tableCreates.sql";
    @SuppressWarnings("serial")
    private static final Map<String, String> PROPS_CONFIG = new HashMap<String, String>() {{
        put("prop", RUN_CONFIG);
        put("commandFile", EXEC_JDBC_FILE);
    }};
    
    public static String getPythonCmd() {
        return isWindowsOS()? "c:\\Python27\\python.exe" : "python";
    }
    public static void runInit() {
        runInit(false);
    }
    
    public static void runInit(boolean useCommand) {
        if (!isWindowsOS()) {
            return;
        }
        for (Entry<String, String> entry: PROPS_CONFIG.entrySet()) {
            if ("commandFile".equals(entry.getKey()) && !useCommand) {
                continue;
            }
            System.setProperty(entry.getKey(), getConfigAbsPath(entry.getValue()));
        }
    }
    
    public static String getConfigAbsPath(String fileName) {
        if (!isWindowsOS()) {
            return fileName;
        }
        return Arrays.stream(new String[]{ ROOT_PATH, fileName}).collect(Collectors.joining("\\"));
    }
    
    public static boolean isWindowsOS() {
        return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
}
