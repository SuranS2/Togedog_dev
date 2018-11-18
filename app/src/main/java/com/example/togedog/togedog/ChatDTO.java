package com.example.togedog.togedog;

public class ChatDTO {

    private String userName;
    private String chat;

    public ChatDTO() {}
    public ChatDTO(String userName, String chat) {
        this.userName = userName;
        this.chat = chat;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getUserName() {
        return userName;
    }

    public String getChat() {
        return chat;
    }
}