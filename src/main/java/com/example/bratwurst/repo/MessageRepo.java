package com.example.bratwurst.repo;

import com.example.bratwurst.model.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo
{
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void callStoredProcedure(BookMovie bookMovie){
        StoredProcedureQuery spInsert = entityManager.createStoredProcedureQuery("PogChamp.bookMovie");

    public List<Message> getConversation(int sender, int receiver);
    public void postMessage(Message msg);
}
