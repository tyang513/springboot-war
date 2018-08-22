package com.github.tyang513.logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 日志修改单元
 * @author tao.yang
 * @date 2018-08-22
 */
public class ChangleLoggerLevelProcessUnit implements IProcessUnit {

    /**
     * 日志框架
     */
    private static final Logger logger = LoggerFactory.getLogger(ChangleLoggerLevelProcessUnit.class);

    private static final String LOG4J_LOGGER_FACTORY = "";

    private static final String LOGBACK_LOGGER_FACTORY = "ch.qos.logback.classic.util.ContextSelectorStaticBinder";

    private static final String LOG4J2_LOGGER_FACTORY = "";

    private Map<String, Object> loggerMap = new HashMap<String, Object>();

    /**
     * logger 框架类型
     * log4j
     * logback
     * log4j2
     * unknow
     */
    private String logFrameworkType = null;

    /**
     * 初始化日志框架
     */
    public void initLoggerFramework() {
        String type = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        logger.info("logger framework : {}", type);
        if (LOG4J_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOG4J;
            Enumeration enumeration = org.apache.log4j.LogManager.getCurrentLoggers();
            while (enumeration.hasMoreElements()) {
                org.apache.log4j.Logger logger = (org.apache.log4j.Logger) enumeration.nextElement();
                if (logger.getLevel() != null) {
                    loggerMap.put(logger.getName(), logger);
                }
            }
            org.apache.log4j.Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
            loggerMap.put(rootLogger.getName(), rootLogger);
        } else if (LOGBACK_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOGBACK;
            ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
            for (ch.qos.logback.classic.Logger logger : loggerContext.getLoggerList()) {
                if (logger.getLevel() != null) {
                    loggerMap.put(logger.getName(), logger);
                }
            }
            ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            loggerMap.put(rootLogger.getName(), rootLogger);
        } else if (LOG4J2_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOG4J2;
//            org.apache.logging.log4j.core.LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
//            Map<String, org.apache.logging.log4j.core.config.LoggerConfig> map = loggerContext.getConfiguration().getLoggers();
//            for (org.apache.logging.log4j.core.config.LoggerConfig loggerConfig : map.values()) {
//                String key = loggerConfig.getName();
//                if (StringUtils.isBlank(key)) {
//                    key = "root";
//                }
//                loggerMap.put(key, loggerConfig);
//            }
        } else {
            logFrameworkType = LogFrameworkType.UNKNOWN;
            logger.error("Log框架无法识别: type={}", type);
        }
    }

    /**
     * 获取logger 列表,从本地容器中获取
     */
    public String getLoggerList() {
        JSONObject result = new JSONObject();
        result.put("loggerFramework", logFrameworkType);
        JSONArray loggerList = new JSONArray();

        for (ConcurrentMap.Entry<String, Object> entry : loggerMap.entrySet()) {
            JSONObject loggerJSON = new JSONObject();
            loggerJSON.put("loggerName", entry.getKey());
            if (logFrameworkType == LogFrameworkType.LOG4J) {
                org.apache.log4j.Logger targetLogger = (org.apache.log4j.Logger) entry.getValue();
                loggerJSON.put("loggerLevel", targetLogger.getLevel().toString());
            } else if (logFrameworkType == LogFrameworkType.LOGBACK) {
                ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) entry.getValue();
                loggerJSON.put("loggerLevel", targetLogger.getLevel().toString());
            } else if (logFrameworkType == LogFrameworkType.LOG4J2) {
//                org.apache.logging.log4j.core.config.LoggerConfig targetLogger = (org.apache.logging.log4j.core.config.LoggerConfig) entry.getValue();
//                loggerJSON.put("loggerLevel", targetLogger.getLevel().toString());
            } else {
                loggerJSON.put("loggerLevel", "Logger的类型未知,无法处理!");
            }
            loggerList.add(loggerJSON);
        }
        result.put("loggerList", loggerList);
        logger.info("getLoggerList: result={}", result.toString());
        return result.toString();
    }

    /**
     * 修改logger日志级别
     *
     * @param data
     * @return
     */
    public String setLogLevel(Object data) {
        logger.info("setLogLevel: data={}", data);
        List<LoggerBean> loggerList = parseJsonData(data);
        if (CollectionUtils.isEmpty(loggerList)) {
            return "";
        }
        for (LoggerBean loggerbean : loggerList) {
            Object logger = loggerMap.get(loggerbean.getName());
            if (logger == null) {
                throw new RuntimeException("需要修改日志级别的Logger不存在");
            }
            if (logFrameworkType == LogFrameworkType.LOG4J) {
                org.apache.log4j.Logger targetLogger = (org.apache.log4j.Logger) logger;
                org.apache.log4j.Level targetLevel = org.apache.log4j.Level.toLevel(loggerbean.getLevel());
                targetLogger.setLevel(targetLevel);
            } else if (logFrameworkType == LogFrameworkType.LOGBACK) {
                ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) logger;
                ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(loggerbean.getLevel());
                targetLogger.setLevel(targetLevel);
            } else if (logFrameworkType == LogFrameworkType.LOG4J2) {
//                org.apache.logging.log4j.core.config.LoggerConfig loggerConfig = (org.apache.logging.log4j.core.config.LoggerConfig) logger;
//                org.apache.logging.log4j.Level targetLevel = org.apache.logging.log4j.Level.toLevel(loggerbean.getLevel());
//                loggerConfig.setLevel(targetLevel);
//                org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
//                ctx.updateLoggers(); // This causes all Loggers to refetch information from their LoggerConfig.
            } else {
                throw new RuntimeException("Logger的类型未知,无法处理!");
            }
        }
        return "success";
    }


    /**
     * @param data
     * @return
     */
    private List<LoggerBean> parseJsonData(Object data) {
        JSONObject result = JSONObject.fromObject(data);
        String logFrameworkType = (String) result.get("loggerFramework");
        logger.info("logFrameworkType = {}", logFrameworkType);
        JSONArray loggerList = result.getJSONArray("loggerList");
        List<LoggerBean> returnList = new ArrayList<>();
        for (int i = 0; i < loggerList.size(); i++) {
            JSONObject object = loggerList.getJSONObject(i);
            String loggerName = object.getString("loggerName");
            String loggerLevel = object.getString("loggerLevel");
            returnList.add(new LoggerBean(loggerName, loggerLevel));
        }
        return returnList;
    }

}
