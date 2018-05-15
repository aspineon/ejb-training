package pl.training.bank.service.operation;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.training.bank.entity.account.Account.ADD_TO_BALANCE_QL;

@Startup
@Singleton
public class SchedulerService {

    private static final long INTEREST_VALUE = 1;
    private static final long FEE_VALUE = -3;

    @Resource
    private TimerService timerService;
    @PersistenceContext(unitName = "bank")
    private EntityManager entityManager;

    @Schedule(hour = "*", minute = "*", persistent = false)
    public void updateInterest() {
        entityManager.createNamedQuery(ADD_TO_BALANCE_QL)
                .setParameter("value", INTEREST_VALUE)
                .executeUpdate();
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "SchedulerService is ready...");
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerConfig.setInfo("Test");
        timerService.createIntervalTimer(new Date(), 1000 * 60 * 2, timerConfig);

        //        ScheduleExpression scheduleExpression = new ScheduleExpression()
//                .hour("*")
//                .minute("*/2");
//        timerService.createCalendarTimer(scheduleExpression, timerConfig);
    }

    @Timeout
    public void onTimeout(Timer timer) {
        entityManager.createNamedQuery(ADD_TO_BALANCE_QL)
                .setParameter("value", FEE_VALUE)
                .executeUpdate();
    }

}
