package com.web.chat.models.users.check;


import com.web.chat.dao.PersonDAO;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DeleteTask extends TimerTask {
    private final String address;
    private final Map<String, Timer> timers;

    private final List<String> online;

    private final String name;

    public DeleteTask(String address, Map<String, Timer> timers, List<String> online, String name)  {
        this.address = address;
        this.timers = timers;
        this.online = online;
        this.name = name;
    }

    @Override
    public void run() {
        //PersonDAO.deleteFromOnline(address);
        online.remove(name);
        timers.remove(address);
    }
}
