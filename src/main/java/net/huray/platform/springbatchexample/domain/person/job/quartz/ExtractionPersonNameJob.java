package net.huray.platform.springbatchexample.domain.person.job.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ExtractionPersonNameJob extends QuartzJobBean {

    private final JobLauncher jobLauncher;
    private final Job extractionPersonNameJob;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            jobLauncher.run(extractionPersonNameJob, new JobParametersBuilder()
                    .addString("message", "[scheduler] " + UUID.randomUUID())
                    .toJobParameters());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
