package net.huray.platform.springbatchexample.domain.person.task;

import net.huray.platform.springbatchexample.domain.person.dto.PersonDto;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@Configuration
public class PersonTaskConfig {

    private BufferedWriter writer;

    @Bean
    public ItemReader<PersonDto> csvReader() {
        return new FlatFileItemReaderBuilder<PersonDto>()
                .name("csvReader")
                .resource(new ClassPathResource("input/person.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(PersonDto.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<PersonDto, String> csvConvertTxtProcessor() {
        return PersonDto::getName;
    }

    @Bean
    public ItemWriter<String> txtWriter() {
        return items -> {
            StringBuilder builder = new StringBuilder();
            items.forEach(
                    item -> builder.append(item).append("\n")
            );
            writer.write(builder.toString());
        };
    }

    @Bean
    public Tasklet createTxtWriterTasklet() {
        return (contribution, chunkContext) -> {
            File txt = new File("./src/main/resources/output/name.txt");

            int i=1;
            while(txt.exists()) {
                txt = new File("./src/main/resources/output/name(" + i + ").txt");
                i++;
            }
            writer = new BufferedWriter(new FileWriter(txt, true));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet closeTxtWriterTasklet() {
        return (contribution, chunkContext) -> {
            writer.close();
            return RepeatStatus.FINISHED;
        };
    }

}
