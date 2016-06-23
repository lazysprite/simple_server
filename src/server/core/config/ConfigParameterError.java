package server.core.config;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ConfigParameterError extends RuntimeException {
    public ConfigParameterError(String s) {
        super(s);
    }
}
