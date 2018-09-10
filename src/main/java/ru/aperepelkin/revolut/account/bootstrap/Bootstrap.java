package ru.aperepelkin.revolut.account.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private boolean waitForExit;
    private static Bootstrap instance;

    public static Bootstrap getInstance() {
        return instance;
    }

    Bootstrap() {
        instance = this;
    }

    private List<Component> components = new ArrayList<>();

    public static BootstrapBuilder builder() {
        return new BootstrapBuilder();
    }

    void addComponent(Component component) {
        components.add(component);
    }

    public void run() {
        components.forEach(Component::run);
        logger.info("Application started");
        if(waitForExit) {
            logger.info("Press CTRL+C for exit");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutdown initiated");
                components.forEach(Component::shutdown);
                synchronized (this) {
                    Bootstrap.this.notify();
                }
            }));
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    logger.error("Unexpected interruption", e);
                    System.exit(-1);
                }
            }
            logger.info("Shutdown completed");
        }
    }

    public void shutdown() {
        if(waitForExit) {
            logger.info("Shutdown immediate");
            System.exit(0);
        } else {
            components.forEach(Component::shutdown);
        }
    }

    public <T extends Component> Optional<T> getComponent(Class<T> clazz) {
        return (Optional<T>) components.stream()
                .filter(c -> clazz.isAssignableFrom(c.getClass()))
                .findFirst();
    }

    void setWaitForExit(boolean waitForExit) {
        this.waitForExit = waitForExit;
    }
}
