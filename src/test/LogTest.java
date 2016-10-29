package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.core.log.Log;

/**
 * Created by Administrator on 2016/6/20.
 */
public class LogTest implements Log {

    @Override
    public void log() {
        Logger test = LoggerFactory.getLogger("LogTest");
        test.debug("abctest");
    }


    public static void main(String[] args) {
        Log log = new LogTest();
        log.log();
    }
}
