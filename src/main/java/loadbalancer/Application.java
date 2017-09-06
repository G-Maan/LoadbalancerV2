package loadbalancer;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Pawel Mielniczuk on 2017-08-30.
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Server starting...");
        SpringApplication.run(Application.class, args);
        logger.info("Server started.");
    }

}
