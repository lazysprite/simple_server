package server.core.actor;

/**
 * Created by Administrator on 2016/10/23.
 */
public class MailBoxFullException extends RuntimeException {
    public MailBoxFullException(String mess) {
        super(mess);
    }
}
