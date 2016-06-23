package server.core.log;

import org.apache.log4j.BasicConfigurator;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/6/16.
 */
public class LogUtil {

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
}
