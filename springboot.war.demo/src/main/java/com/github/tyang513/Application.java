package com.github.tyang513;

import com.github.tyang513.logger.ChangleLoggerLevelProcessUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tao.yang
 * @date 2018-08-17
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("=========== Application.main =============");
        ChangleLoggerLevelProcessUnit changleLoggerLevelProcessUnit = new ChangleLoggerLevelProcessUnit();
        changleLoggerLevelProcessUnit.initLoggerFramework();
        changleLoggerLevelProcessUnit.getLoggerList();
    }


}
