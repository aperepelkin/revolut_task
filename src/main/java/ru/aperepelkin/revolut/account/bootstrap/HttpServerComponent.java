package ru.aperepelkin.revolut.account.bootstrap;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aperepelkin.revolut.account.AccountEndpoint;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class HttpServerComponent implements Component {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerComponent.class);

    private HttpServer httpServer;
    private int port;

    HttpServerComponent() {
    }

    @Override
    public void run(){
        logger.info("Starting grizzly server");
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();

        ResourceConfig config = new ResourceConfig(AccountEndpoint.class);

        JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        config.register(jacksonJaxbJsonProvider);

        httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);
        try {
            httpServer.start();
            logger.info("Grizzly server started");
        } catch (IOException e) {
            logger.error("Starting server error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        httpServer.shutdown();
        logger.info("Grizzly server stopped");
    }

    void setPort(int port) {
        this.port = port;
    }
}
