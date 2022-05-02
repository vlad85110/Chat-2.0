package com.web.chat.dao;

import com.web.chat.exceptions.enter.EnterException;
import com.web.chat.exceptions.enter.IncorrectPasswordException;
import com.web.chat.exceptions.enter.NoSuchUserException;
import com.web.chat.exceptions.register.InvalidNameExceptions;
import com.web.chat.exceptions.register.InvalidPasswordException;
import com.web.chat.exceptions.register.RegisterException;
import com.web.chat.models.Status;
import com.web.chat.models.users.Person;
import com.web.chat.rowmappers.OnlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {
    private static JdbcTemplate template;

    @Autowired
    public PersonDAO(JdbcTemplate template) {
        PersonDAO.template = template;
    }

    public void enter(Person person, String address) throws EnterException {
        if (!isRegistered(person)) {
            try {
                template.queryForObject("SELECT * FROM people WHERE name='" + person.getName() + "'",
                        new BeanPropertyRowMapper<>(Person.class));
            } catch (EmptyResultDataAccessException ex) {
                throw new NoSuchUserException();
            }
            throw new IncorrectPasswordException();
        }

        addAuthorized(person, address);
    }

    public void autoEnter(Person person, String address) {
        addAuthorized(person, address);
    }

    public boolean isRegistered(Person person) {
        try {
            template.queryForObject("SELECT * FROM people WHERE name='" + person.getName() +
                            "'AND password='" + person.getPassword() + "'",
                    new BeanPropertyRowMapper<>(Person.class));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    public void addAuthorized(Person person, String address) {
        try {
            template.queryForMap("SELECT * FROM authorized_users WHERE address='" + address + "'");
        } catch (EmptyResultDataAccessException e) {
            template.update("INSERT INTO authorized_users VALUES (?, ?)", person.getName(), address);
        }
    }

    public void register(Person person) throws RegisterException {
        if (person.getPassword() == 0) {
            throw new InvalidPasswordException("empty password");
        }

        try {
            template.queryForMap("SELECT * FROM people WHERE name='" + person.getName() + "'");
        } catch (EmptyResultDataAccessException e) {
            template.update("INSERT INTO people VALUES (?, ?)", person.getName(), person.getPassword());
            return;
        }
        throw new InvalidNameExceptions("this user already exists");
    }

    public static boolean isAuthorized(String address) {
        try {
            template.queryForMap("SELECT * FROM authorized_users WHERE address='" + address + "'");
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    public static Person getPersonByAddress(String address) {
        return template.queryForObject("SELECT * FROM authorized_users WHERE address='" + address + "'",
                new BeanPropertyRowMapper<>(Person.class));
    }

    public void exit(String address) {
        template.update("DELETE FROM authorized_users WHERE address='" + address +"'");
    }

    public static Person getPersonById(int id) {
        return template.queryForObject("SELECT * FROM people WHERE id='" + id + "'",
                new BeanPropertyRowMapper<>(Person.class));
    }

    public static int getIdByName(String name) {
        var person =  template.queryForObject("SELECT * FROM people WHERE name='" + name + "'",
                new BeanPropertyRowMapper<>(Person.class));
        assert person != null;
        return person.getId();
    }

    public Status getStatus(int id) {
        var person = getPersonById(id);

        try {
            template.queryForMap("SELECT * FROM people_online WHERE name='" + person.getName() + "'");
        } catch (EmptyResultDataAccessException e) {
            return new Status(null, "Offline");
        }
        return new Status("Online", null);
    }
}
