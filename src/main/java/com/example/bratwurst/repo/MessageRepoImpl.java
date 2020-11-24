package com.example.bratwurst.repo;

import com.example.bratwurst.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageRepoImpl implements MessageRepo
{
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Message> getConversation(int sender, int receiver)
    {
        String sql = "select m.id, m.sender, m.receiver, m.content, m.file, m.timestamp, u1.username as sender_username, u2.username as receiver_username from messages m \n" +
                "join users u1 on m.sender = u1.id \n" +
                "join users u2 on m.receiver = u2.id \n" +
                "where (sender = ? or receiver = ?) and (sender = ? or receiver = ?) order by timestamp asc";

        List<Message> messages = jdbc.query(sql, resultSet ->
        {
            List<Message> msgs = new ArrayList<>();

            while(resultSet.next())
            {
                Message msg = new Message();
                msg.setContent(resultSet.getString("content"));
                msg.setFile(resultSet.getBoolean("file"));
                msg.setId(resultSet.getInt("id"));
                msg.setReceiver(resultSet.getInt("receiver"));
                msg.setSender(resultSet.getInt("sender"));
                msg.setTimestamp(resultSet.getDate("timestamp").toString());

                msg.setReceiverUsername(resultSet.getString("receiver_username"));
                msg.setSenderUsername(resultSet.getString("sender_username"));

                msgs.add(msg);
            }

            return msgs;
        }, sender, sender, receiver, receiver);

        return messages;
    }

    @Override
    public void postMessage(Message msg)
    {
        String sql = "insert into messages (sender, receiver, content, file, timestamp) values (?, ?, ?, ?, ?)";

        jdbc.update(sql, msg.getSender(), msg.getReceiver(), msg.getContent(), msg.isFile(), msg.getTimestamp());
    }
}
