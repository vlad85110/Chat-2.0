package com.web.chat.models.users.check;

import com.web.chat.dao.PersonDAO;
import com.web.chat.models.Status;
import com.web.chat.models.users.Person;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

public class PeopleOnlineChecker {
    private static final Map<String, Timer> timers = new HashMap<>();

    private static final List<String> online = new ArrayList<>();

    private final static int timeout = 120;//secs

    public static void registerRequest(String address) {
        var person = PersonDAO.getPersonByAddress(address);
        var name = person.getName();

        if (!timers.containsKey(address)) {
            if (!online.contains(name)) {
                online.add(name);
            }
        }

        timers.computeIfPresent(address, (s, timer) -> {
            timer.cancel();
            timer.purge();
            return null;
        });

        Timer timer = new Timer();
        var task = new DeleteTask(address, timers, online, name);

        timer.schedule(task, timeout * 1000);
        timers.put(address, timer);
    }

    public static void stop() {
        for (var i : timers.entrySet()) {
            i.getValue().cancel();
            i.getValue().purge();
        }
    }

    public static List<String> showOnline() {
        return online;
    }

    public static void deleteFromOnline(String address) {
        var timer = timers.remove(address);
        timer.cancel();
        timer.purge();
        online.remove(address);
    }

    public static Status getStatus(Person person) {
        var name = person.getName();

        if (online.contains(name)) {
            return new Status("Online", null);
        }

        return new Status(null, "Offline");
    }
}
