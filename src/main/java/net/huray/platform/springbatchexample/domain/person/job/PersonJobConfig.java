package net.huray.platform.springbatchexample.domain.person.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PersonJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final Step fillTxtStep;
    private final Step createTxtWriterStep;
    private final Step closeTxtWriterStep;

    @Bean
    public Job extractionPersonNameJob() {
        return jobBuilderFactory.get("extractionPersonNameJob")
                .start(createTxtWriterStep)
                .next(fillTxtStep)
                .next(closeTxtWriterStep)
                .build();
    }

}
