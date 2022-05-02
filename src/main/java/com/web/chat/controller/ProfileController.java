package com.web.chat.controller;

import com.web.chat.dao.PersonDAO;
import com.web.chat.loggingfilter.ClientInfo;
import com.web.chat.models.users.check.PeopleOnlineChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @GetMapping
    private String getProfile(@RequestParam("id") int id, Model model) {
        var person = PersonDAO.getPersonById(id);
        model.addAttribute("person", person);
        model.addAttribute("status", PeopleOnlineChecker.getStatus(person));
        return "profile";
    }
}
