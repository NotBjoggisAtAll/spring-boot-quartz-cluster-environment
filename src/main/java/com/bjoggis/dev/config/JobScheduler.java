package com.bjoggis.dev.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * This class extends the SchedulerFactoryBean with possibility to set the list
 * of triggers as a map, where keys are bean names and values are strings of
 * boolean values (true|false|1|0) specifying if bean should be enabled or not.
 *
 * <p>This to be able to enable or disable jobs using a properties file and a
 * {@link PropertyPlaceholderConfigurer}.
 */
public class JobScheduler extends SchedulerFactoryBean {

    private ApplicationContext applicationContext;
    private Map<String, Boolean> scheduledTriggers;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        super.setApplicationContext(applicationContext);
    }

    public void setScheduledTriggers(Map<String, Boolean> scheduledTriggers) {
        this.scheduledTriggers = scheduledTriggers;
    }

    @Override
    protected void startScheduler(Scheduler scheduler, int startupDelay) throws SchedulerException {
        super.startScheduler(scheduler, startupDelay);

        for (Map.Entry<String, Boolean> entry : scheduledTriggers.entrySet()) {
            final Boolean triggerEnabled = entry.getValue();

            if (triggerEnabled != null) {
                final Trigger trigger = applicationContext.getBean(entry.getKey(), Trigger.class);
                if (trigger != null) {
                    if (triggerEnabled) {
                        if (!this.getScheduler().checkExists(trigger.getKey())) {
                            this.getScheduler().scheduleJob(trigger);
                        }
                    } else {
                        this.getScheduler().unscheduleJob(trigger.getKey());
                    }
                }
            }

        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final List<Trigger> triggers = new ArrayList<>(scheduledTriggers.size());

        for (Map.Entry<String, Boolean> entry : scheduledTriggers.entrySet()) {
            final Trigger trigger = applicationContext.getBean(entry.getKey(), Trigger.class);
            triggers.add(trigger);
        }

        super.setTriggers((Trigger[]) triggers.toArray(new Trigger[triggers.size()]));
        super.afterPropertiesSet();
    }

}
