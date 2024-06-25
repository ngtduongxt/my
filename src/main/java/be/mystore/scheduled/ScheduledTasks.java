package be.mystore.scheduled;

import be.mystore.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private UserServiceImpl userService;

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens() {
        userService.removeExpTokens();
    }
}
