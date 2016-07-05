package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ParserTask;
import modules.api.ParseCommander;
import modules.api.ParseManager;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import static models.ParserTask.Status.ERROR;

/**
 * Copyright(C) 2015 Ghent University
 *
 * @author Stefaan Vermassen (Stefaan.Vermassen@UGent.be)
 * @since 29/02/2016.
 */
public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }



    public static Result createTask(String url, String prefix) {
        ParserTask parserEntity = new ParserTask();
        parserEntity.save();
        ParseCommander c = ParseManager.getManager().createCommanderForTask(parserEntity);
        c.parseUrl(parserEntity.getId(), prefix, url);
        ObjectNode result = Json.newObject();
        result.put("status", parserEntity.getStatus().toString());
        result.put("id", parserEntity.getId());
        return ok(result);
    }

    public static Result checkTask(long id) {
        ParserTask task = ParserTask.FIND.where().eq("id", id).findUnique();
        if (task == null) {
            return ok(taskDoesntExist(id));
        }
        return ok(getResult(task));
    }

    private static ObjectNode taskDoesntExist(long id) {
        ObjectNode result = Json.newObject();
        result.put("id", id);
        result.put("message", "There is no task for this id");
        return result;
    }

    private static ObjectNode getResult(ParserTask task) {
        ObjectNode result = Json.newObject();
        result.put("status", task.getStatus().toString());
        if (task.getStatus().equals(ERROR) && task.getResult() != null && !task.getResult().isEmpty()) {
            result.put("reason", task.getResult());
        }
        result.put("id", task.getId());

        return result;
    }

    public static Result getTaskResult(long id) {
        ParserTask task = ParserTask.FIND.where().eq("id", id).findUnique();

        if (task == null) {
            return ok(taskDoesntExist(id));
        }

        if (task.getStatus().equals(ERROR)) {
            return ok(getResult(task));
        } else {
            return ok(Json.parse(task.getResult() == null ? "{}" : task.getResult()));
        }
    }
}
