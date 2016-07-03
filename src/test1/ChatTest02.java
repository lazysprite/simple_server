package test1;

import game.chat.*;

/**
 * Created by Administrator on 2016/6/27.
 */
public class ChatTest02 {

    public static void main(String[] args) {
        ChatService service = new ChatServiceImp();
        long chatId = 100001;
        String chatName = "tom";
        long chatId2 = 100002;
        String chatName2 = "jerry";
        long groupId = 1;
        String groupName = "1服";

        service.registerChatService(chatId, chatName, new ChatCallBack() {
            @Override
            public void chatCallBack(Dialogue dialogue) {
                System.out.println(chatName + "|reciver|" + dialogue.getMessage() + "|from|" + dialogue.getSpeakerId());
            }
        });
        service.addGroup(chatId, groupId, groupName);

        service.registerChatService(chatId2, chatName2, new ChatCallBack() {
            @Override
            public void chatCallBack(Dialogue dialogue) {
                System.out.println(chatName2 + "|reciver|" + dialogue.getMessage() + "|from|" + dialogue.getSpeakerId());
            }
        });
        service.addGroup(chatId2, groupId, groupName);

        service.communicate(chatId, groupId, "世界聊天");
    }

}
