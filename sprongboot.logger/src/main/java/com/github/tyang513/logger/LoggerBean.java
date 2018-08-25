package com.github.tyang513.logger;

/**
 * @author tao.yang
 * @date 2018-08-22
 */
public class LoggerBean {

    private String name;

    private String level;

    public LoggerBean(String name, String level){
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
