package game.chat;

/**
 * Created by Administrator on 2016/6/27.
 */
public interface Dialogue {

    long getSpeakerId();

    long getListenerId();

    String getMessage();

    void setCallBackCase(CallBackCase callBackCase);

    CallBackCase getCallBackCase();

    long getDialogueTime();
}
