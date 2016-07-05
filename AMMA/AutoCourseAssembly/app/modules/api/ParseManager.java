package modules.api;

import akka.actor.ActorRef;
import akka.actor.Props;
import models.ParserTask;
import play.libs.Akka;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Copyright(C) 2015 Ghent University
 *
 * @author Stefaan Vermassen (Stefaan.Vermassen@UGent.be)
 * @since 29/02/2016.
 */

public class ParseManager {

    private static final ParseManager manager = new ParseManager();
    private ConcurrentMap<Long, ParseCommander> tasks;

    public ParseManager() {
        tasks = new ConcurrentHashMap<>();
    }

    public static ParseManager getManager() {
        return manager;
    }

    public ParseCommander createCommanderForTask(ParserTask task) {
        ActorRef parseActor = Akka.system().actorOf(
                Props.create(ParseActor.class,
                        () -> new ParseActor()));
        ParseCommander commander = new ParseCommander(parseActor);
        tasks.put(task.getId(), commander);
        return commander;
    }

    public ParseCommander getCommanderForTask(ParserTask task) {
        ParseCommander c = tasks.get(task.getId());
        if (c == null) {
            throw new IllegalArgumentException("Task is not initialized yet. Use createCommander first.");
        }
        return c;
    }
}
