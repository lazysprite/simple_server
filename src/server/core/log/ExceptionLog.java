package server.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/6/21.
 */
public class ExceptionLog implements Log {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionLog.class);

    private Throwable exception;

    public ExceptionLog(Throwable throwable) {
        exception = throwable;
    }

    @Override
    public void log() {
        logger.error(LogUtil.getStackTrace(exception));
    }
}
