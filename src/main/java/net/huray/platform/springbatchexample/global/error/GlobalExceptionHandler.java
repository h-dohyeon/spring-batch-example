package net.huray.platform.springbatchexample.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GlobalExceptionHandler implements ExceptionHandler {

    @Override
    public void handleException(RepeatContext context, Throwable throwable) {
        log.error("[ERROR] " + throwable.getMessage());
    }

}
