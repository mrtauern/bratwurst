package com.example.bratwurst.model;

import java.util.Date;

public class Message
{
    private int id;
    private int sender;
    private int receiver;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private String timestamp;
    private boolean file;

    public String getSenderUsername()
    {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername)
    {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername()
    {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername)
    {
        this.receiverUsername = receiverUsername;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getSender()
    {
        return sender;
    }

    public void setSender(int sender)
    {
        this.sender = sender;
    }

    public int getReceiver()
    {
        return receiver;
    }

    public void setReceiver(int receiver)
    {
        this.receiver = receiver;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public boolean isFile()
    {
        return file;
    }

    public void setFile(boolean file)
    {
        this.file = file;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", file=" + file +
                '}';
    }
}
