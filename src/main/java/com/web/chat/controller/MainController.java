package com.web.chat.controller;


import com.web.chat.exceptions.enter.EnterException;
import com.web.chat.exceptions.register.RegisterException;
import com.web.chat.dao.PersonDAO;
import com.web.chat.exceptions.error.Error;
import com.web.chat.models.users.Person;
import com.web.chat.loggingfilter.ClientInfo;
import com.web.chat.models.users.check.PeopleOnlineChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class MainController {
    private final PersonDAO personDAO;
    private final ClientInfo info;
    private String error;

    @Autowired
    public MainController(PersonDAO personDAO, ClientInfo info) {
        this.personDAO = personDAO;
        this.info = info;
    }

    @GetMapping
    public String mainScreen() {
        return "main";
    }

    @GetMapping("/enter")
    public String showAuthorization(@ModelAttribute("person") Person person,
                                    @ModelAttribute("error") Error error, HttpServletRequest request) {
        var address = info.printClientInfo(request);

        if (PersonDAO.isAuthorized(address)) {
            var user = PersonDAO.getPersonByAddress(address);
            personDAO.autoEnter(user, address);
            return "redirect:/chat";
        }

        error.setText(this.error);
        this.error = null;
        return "enter";
    }

    @PostMapping("/enter")
    public String authorize(@ModelAttribute("person") Person person, HttpServletRequest request) {
        error = null;
        var address = info.printClientInfo(request);
        person.setPassword(person.getStrPassword().hashCode());

        try {
            personDAO.enter(person, address);
        } catch (EnterException e) {
            error = e.getMessage();
            return "redirect:/enter";
        }
        return "redirect:/chat";
    }

    @GetMapping("/register")
    public String showRegistration(@ModelAttribute("person") Person person, @ModelAttribute("error") Error error) {
        error.setText(this.error);
        this.error = null;
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("person") Person person, HttpServletRequest request) {
        error = null;
        var address = info.printClientInfo(request);
        person.setPassword(person.getStrPassword().hashCode());

        try {
            personDAO.register(person);
        } catch (RegisterException e) {
            e.printStackTrace();
            error = e.getMessage();
            return "redirect:/register";
        }

        personDAO.autoEnter(person, address);

        return "redirect:/chat";
    }

    @GetMapping("/exit")
    public String exit(HttpServletRequest request) {
        var address = info.printClientInfo(request);
        PeopleOnlineChecker.deleteFromOnline(address);
        personDAO.exit(address);
        return "redirect:/";
    }
}
