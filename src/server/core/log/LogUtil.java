package server.core.log;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/6/16.
 */
public class LogUtil {

    static {
        init();
    }

    private static Logger console = LoggerFactory.getLogger("console");

    public static void init() {
        BasicConfigurator.configure();
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try{
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally  {
            pw.close();
        }
    }

    public static void info(String s, Object ...args) {
        console.info(s, args);
    }

    public static void error(String s, Object... args) {
        console.error(s, args);
    }
}
