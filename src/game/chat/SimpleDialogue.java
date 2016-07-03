package game.chat;

/**
 * Created by Administrator on 2016/6/27.
 */
public class SimpleDialogue implements Dialogue {

    private final long speakerId;
    private final long listenerId;
    private final String message;
    private final long millis;
    private CallBackCase callBackCase;

    public SimpleDialogue(long speaker, long listener, String message) {
        speakerId = speaker;
        listenerId = listener;
        this.message = message;
        millis = System.currentTimeMillis();
        callBackCase = CallBackCase.NORMAL;
    }

    @Override
    public long getSpeakerId() {
        return speakerId;
    }

    @Override
    public long getListenerId() {
        return listenerId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setCallBackCase(CallBackCase callBackCase) {
        this.callBackCase = callBackCase;
    }

    @Override
    public CallBackCase getCallBackCase() {
        return callBackCase;
    }

    @Override
    public long getDialogueTime() {
        return millis;
    }
}
