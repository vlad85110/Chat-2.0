package com.web.chat.dao;

import com.web.chat.models.messages.Message;
import com.web.chat.rowmappers.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageDAO {
    private final JdbcTemplate template;

    @Autowired
    public MessageDAO(JdbcTemplate template) {
        this.template = template;
    }

    public List<Message> showAll() {
        return template.query("select * from messages order by date",new BeanPropertyRowMapper<>(Message.class));
    }

    public void sendMessage(Message message, String address) {
        var name = template.query("SELECT * FROM authorized_users WHERE address='" + address + "'",
                new MessageMapper()).stream().findAny().orElse(null);
        var id = PersonDAO.getIdByName(name);

        template.update("INSERT INTO messages VALUES (?, ?, ?, ?)", message.getText(),
               id, name, message.getTime());
    }
}
