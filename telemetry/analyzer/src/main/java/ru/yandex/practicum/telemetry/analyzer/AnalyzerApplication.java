package ru.yandex.practicum.telemetry.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.telemetry.analyzer.starter.HubStarter;
import ru.yandex.practicum.telemetry.analyzer.starter.SnapshotStarter;

@SpringBootApplication
public class AnalyzerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerApplication.class, args);
        HubStarter hub = context.getBean(HubStarter.class);
        SnapshotStarter snapshot = context.getBean(SnapshotStarter.class);

        Thread hubEventsThread = new Thread(hub);
        hubEventsThread.start();
        snapshot.start();
    }
}
