package models;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonView;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.JsonHelper;
import utils.observer.StateChangedEvent;
import utils.observer.StateListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Copyright(C) 2015 Ghent University
 *
 * @author Stefaan Vermassen (Stefaan.Vermassen@UGent.be)
 * @since 29/02/2016.
 */
@Entity
@JsonRootName("parsertask")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParserTask extends Model {

    public static final Finder<Long, ParserTask> FIND = new Finder<>(Long.class, ParserTask.class);
    @Version
    @JsonIgnore
    public Long version;

    @JsonView(JsonHelper.Summary.class)
    @Id
    private Long id;

    @JsonView(JsonHelper.Summary.class)
    @Constraints.Required
    private Status status;
    @Column(columnDefinition = "TEXT")
    private String result;

    public ParserTask() {
        status = Status.QUEUED;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long stamp) {
        this.version = stamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public enum Status {
        QUEUED,
        DOWNLOADING,
        PARSING,
        RECOGNISING,
        WRITING_WITHOUT_TAGS,
        WRITING_WITH_TAGS,
        ERROR,
        PARSER_ERROR,
        RECOGNISING_ERROR,
        DONE_WITHOUT_TAGS,
        DONE_WITH_TAGS,
    }
}
