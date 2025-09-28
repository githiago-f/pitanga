package br.edu.ifrs.poa.pitanga_code.app.services.mediator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.EventHandler;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.Mediator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryMediator implements CommandLineRunner, Mediator {
    private final BlockingQueue<Message<?>> queue;
    private final ExecutorService executor;
    private final ApplicationContext context;

    private final Integer workers;

    @Autowired
    public InMemoryMediator(
            @Value("${pitanga.sandbox.workers-num}") Integer workers,
            ApplicationContext context) {

        this.workers = workers;

        this.context = context;
        executor = Executors.newFixedThreadPool(workers,
                Thread.ofPlatform().name("code-worker-", 1).factory());
        queue = new LinkedBlockingQueue<>(1024);
    }

    @Override
    public <T> void dispatch(Message<T> message) {
        queue.add(message);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < workers; i++) {
            log.info("Initializing worker {}", i);
            executor.submit(() -> {
                while (true) {
                    var message = queue.take();

                    if (message == null)
                        continue;

                    try {
                        if (!context.containsBean(message.type())) {
                            continue;
                        }

                        var handler = context.getBean(message.type(), EventHandler.class);

                        handler.execute(message.data());
                    } catch (RuntimeException exception) {
                        log.error("Failed at delivering message {}", exception);
                        queue.add(message);
                    }
                }
            });
        }
    }
}
