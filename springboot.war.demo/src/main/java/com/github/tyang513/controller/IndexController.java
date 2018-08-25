package com.github.tyang513.controller;

import com.github.tyang513.logger.ChangleLoggerLevelProcessUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tao.yang
 * @date 2018-08-17
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        logger.warn(" warn 级别日志打印 ");
        logger.info(" info 级别日志打印 ");
        logger.error(" error 级别日志打印 ");
        return "index";
    }

    @RequestMapping(value = "/changeLoggerLevel", method = RequestMethod.GET)
    @ResponseBody
    public String changeLoggerLevel(String loggerLevel){
        logger.info(" loggerLevel {} ", loggerLevel);
        ChangleLoggerLevelProcessUnit processUnit = new ChangleLoggerLevelProcessUnit();
        processUnit.initLoggerFramework();
        String loggerList = processUnit.getLoggerList();
        processUnit.setLogLevel("{\"loggerFramework\":\"log4j\",\"loggerList\":[{\"loggerName\":\"root\",\"loggerLevel\":\"" + loggerLevel + "\"}]}");
        return loggerLevel;
    }



    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    @ResponseBody
    public void exception(String loggerLevel){
        try{
            Integer i = null;
            System.out.println(i.toString());
        }
        catch (Exception e){
            logger.error("空指针异常", e);
        }

    }

}
