package ru.aperepelkin.revolut.account.bootstrap;

public class BootstrapBuilder {

    private Bootstrap bootstrap = new Bootstrap();

    public BootstrapBuilder daemon() {
        bootstrap.setWaitForExit(true);
        return this;
    }

    public class HttpServerStep {

        private HttpServerComponent component;

        HttpServerStep() {
            component = new HttpServerComponent();
            bootstrap.addComponent(component);
        }

        public HttpServerStep port(int port) {
            component.setPort(port);
            return this;
        }

        public BootstrapBuilder and() {
            return BootstrapBuilder.this;
        }

        public Bootstrap build() {
            return BootstrapBuilder.this.build();
        }
    }

    public class DatabaseStep {

        private DatabaseComponent component;
        private MigrationHandler migrate = new MigrationHandler();

        DatabaseStep() {
            this.component = new DatabaseComponent();
            bootstrap.addComponent(component);
        }

        public DatabaseStep migrate() {
            component.addAfterHandler(migrate);
            return this;
        }

        public DatabaseStep demo() {
            migrate.addLocation("classpath:demo.db.migration");
            return this;
        }

        public BootstrapBuilder and() {
            return BootstrapBuilder.this;
        }

        public DatabaseStep jdbc(String url) {
            component.setJdbc(url);
            return this;
        }

        public Bootstrap build() {
            return BootstrapBuilder.this.build();
        }
    }

    public HttpServerStep httpServer() {
        return new HttpServerStep();
    }

    public DatabaseStep database() {
        return new DatabaseStep();
    }

    public Bootstrap build() {
        return bootstrap;
    }
}
