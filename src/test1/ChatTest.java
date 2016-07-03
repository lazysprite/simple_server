package test1;

import game.chat.ChatCallBack;
import game.chat.ChatService;
import game.chat.ChatServiceImp;
import game.chat.Dialogue;

/**
 * Created by Administrator on 2016/6/27.
 */
public class ChatTest {

    public static void main(String[] args) {

        ChatService service = new ChatServiceImp();
        long chatId = 100001;
        String chatName = "tom";
        long chatId2 = 100002;
        String chatName2 = "jerry";
        service.registerChatService(chatId, chatName, new ChatCallBack() {
            @Override
            public void chatCallBack(Dialogue dialogue) {
                System.out.println(chatName + "|" + dialogue.getMessage());
            }
        });
        service.registerChatService(chatId2, chatName2, new ChatCallBack() {
            @Override
            public void chatCallBack(Dialogue dialogue) {
                System.out.println(chatName + "|" + dialogue.getMessage());
            }
        });

        service.communicate(chatId, chatId2, "hello world.");
    }

}
