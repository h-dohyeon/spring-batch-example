package net.huray.platform.springbatchexample;

import lombok.extern.slf4j.Slf4j;
import net.huray.platform.springbatchexample.domain.person.dto.MyMessage;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@SpringBootApplication
@EnableAsync
public class SpringBatchExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchExampleApplication.class, args);
    }


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job extractionPersonNameJob;


    @Bean
    public Function<Flux<MyMessage>, Mono<Void>> direct() {
        return myMessageFlux -> myMessageFlux.doOnNext((myMessage) -> {
                            log.info("##### message for direct : {}", myMessage.getMessage());
                            try {
                                jobLauncher.run(extractionPersonNameJob, new JobParametersBuilder()
                                        .addString("message", "[rabbitmq] " + myMessage.getMessage())
                                        .toJobParameters());
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
                )
                .then();
    }

}
