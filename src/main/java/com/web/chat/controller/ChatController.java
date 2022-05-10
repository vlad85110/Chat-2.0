package com.web.chat.controller;

import com.web.chat.dao.MessageDAO;
import com.web.chat.dao.PersonDAO;
import com.web.chat.models.messages.JSonMessage;
import com.web.chat.models.messages.Message;
import com.web.chat.loggingfilter.ClientInfo;
import com.web.chat.models.users.check.PeopleOnlineChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final MessageDAO messageDAO;
    private final ClientInfo info;

    @Autowired
    public ChatController(MessageDAO messageDAO, ClientInfo info) {
        this.messageDAO = messageDAO;
        this.info = info;
    }

    @GetMapping()
    public String chatFrame(Model model, @ModelAttribute("message") Message message, HttpServletRequest request) {
        var session = info.printClientInfo(request);
        PeopleOnlineChecker.registerRequest(session);
        if (!PersonDAO.isAuthorized(session)) {
            return "redirect:/enter";
        }

        model.addAttribute("messages", messageDAO.showAll());
        return "chat";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/messages")
    public Message bCastMessage(@Payload JSonMessage message) {
        if (message.getText().isEmpty()){
            return null;
        }
        var sendMessage = new Message(message.getText());
        var author = PersonDAO.getPersonByAddress(message.getSession()).getName();

        sendMessage.setId(PersonDAO.getIdByName(author));
        sendMessage.setAuthor(author);
        messageDAO.sendMessage(sendMessage, message.getSession());

        return sendMessage;
    }

    @MessageMapping("/list")
    @SendTo("/topic/list")
    public List<Message> sendAllMessages() {
        return messageDAO.showAll();
    }

    @GetMapping("/online")
    @ResponseBody
    public List<String> sendNewOnline(HttpServletRequest request) {
        var session = info.printClientInfo(request);
        PeopleOnlineChecker.registerRequest(session);
        return PeopleOnlineChecker.showOnline();
    }

    @ResponseBody
    @GetMapping("/session")
    public String sendSession(HttpServletRequest request) {
        return info.printClientInfo(request);
    }

}
