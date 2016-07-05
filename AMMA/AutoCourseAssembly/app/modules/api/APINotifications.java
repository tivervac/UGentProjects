package modules.api;

import java.io.Serializable;

/**
 * Copyright(C) 2015 Ghent University
 *
 * @author Stefaan Vermassen (Stefaan.Vermassen@UGent.be)
 * @since 29/02/2016.
 */
public class APINotifications {

    public static class ParseRequestNotification implements Serializable {
        private long taskId;
        private String url;
        private String prefix;

        public ParseRequestNotification(long taskId, String url, String prefix) {
            this.taskId = taskId;
            this.url = url;
            this.prefix = prefix;
        }

        public String getUrl() {
            return url;
        }

        public String getPrefix() {return prefix; }

        public boolean hasPrefix() {return prefix.isEmpty(); }

        public long getTaskId() {
            return taskId;
        }
    }
}
