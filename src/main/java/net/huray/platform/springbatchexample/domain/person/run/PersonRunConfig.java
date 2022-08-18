package net.huray.platform.springbatchexample.domain.person.run;

import net.huray.platform.springbatchexample.domain.person.job.quartz.ExtractionPersonNameJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonRunConfig {

    @Bean
    public Trigger extractionPersonNameJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(1).withRepeatCount(1);

        return TriggerBuilder.newTrigger()
                .forJob(extractionPersonNameJobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail extractionPersonNameJobDetail() {
        return JobBuilder.newJob(ExtractionPersonNameJob.class)
                .storeDurably()
                .build();
    }

}
