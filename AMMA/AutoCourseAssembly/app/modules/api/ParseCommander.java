package modules.api;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Copyright(C) 2015 Ghent University
 *
 * @author Stefaan Vermassen (Stefaan.Vermassen@UGent.be)
 * @since 29/02/2016.
 */

public class ParseCommander {
    private static final ActorSystem system = ActorSystem.create("ParseAPI");
    private static final Timeout TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));
    private final ActorRef parseActor;

    public ParseCommander(final ActorRef parseActor) {
        this.parseActor = parseActor;
    }

    public void parseUrl(long taskId, String prefix, String url) {
        parseActor.tell(new APINotifications.ParseRequestNotification(taskId, url, prefix), ActorRef.noSender());
    }


}
