package com.github.tyang513.logger;

/**
 * @author tao.yang
 * @date 2018-08-22
 */
public interface IProcessUnit {

    /**
     * 初始化日志框架
     */
    void initLoggerFramework();

    /**
     * 获取logger级别配置信息
     * @return
     */
    public String getLoggerList();

    /**
     * 修改日志级别
     * @param object
     * @return
     */
    public String setLogLevel(Object object);


}
