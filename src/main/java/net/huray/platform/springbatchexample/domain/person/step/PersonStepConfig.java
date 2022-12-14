package net.huray.platform.springbatchexample.domain.person.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.huray.platform.springbatchexample.domain.person.dto.PersonDto;
import net.huray.platform.springbatchexample.global.error.GlobalExceptionHandler;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PersonStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final GlobalExceptionHandler globalExceptionHandler;

    private final ItemReader<PersonDto> csvReader;
    private final ItemProcessor<PersonDto, String> csvConvertTxtProcessor;
    private final ItemWriter<String> txtWriter;
    private final Tasklet createTxtWriterTasklet;
    private final Tasklet closeTxtWriterTasklet;

    @Bean
    @JobScope
    public Step fillTxtStep(@Value("#{jobParameters['message']}")String message) {
        log.info("##### JOB message for direct : {}", message);
        return stepBuilderFactory.get("fillTxtStep")
                .<PersonDto, String> chunk(10)
                .reader(csvReader)
                .processor(csvConvertTxtProcessor)
                .writer(txtWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(1)
//                .retry(FlatFileParseException.class)
//                .retryLimit(1)
                .exceptionHandler(globalExceptionHandler)
                .build();
    }

    @Bean
    public Step createTxtWriterStep() {
        return stepBuilderFactory.get("createTxtWriterStep")
                .tasklet(createTxtWriterTasklet)
                .build();
    }

    @Bean
    public Step closeTxtWriterStep() {
        return stepBuilderFactory.get("closeTxtWriterStep")
                .tasklet(closeTxtWriterTasklet)
                .build();
    }

}
