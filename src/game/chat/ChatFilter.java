package game.chat;

/**
 * Created by Administrator on 2016/7/4.
 */
public interface ChatFilter {

    boolean filter(Dialogue dialogue);

    CallBackCase getCase();
}
