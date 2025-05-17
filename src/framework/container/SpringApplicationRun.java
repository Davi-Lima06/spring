package framework.container;

import java.io.IOException;

public class SpringApplicationRun {

    public static void start() throws IOException {
        ApplicationContext context = new ApplicationContext("projetoteste");
        MiniHttpServer server = new MiniHttpServer(context);
        server.start(8080);
    }
}
