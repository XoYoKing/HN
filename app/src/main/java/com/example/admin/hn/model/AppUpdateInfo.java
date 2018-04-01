package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/11/27 10:51
 */
public class AppUpdateInfo {
    private List<App> Documents;

    private String status;

    private String message;

    public List<App> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<App> documents) {
        Documents = documents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class App {
        private String explain;
        private String downloadurl;
        private String versionnumber;
        private String versionname;

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getDownloadurl() {
            return downloadurl;
        }

        public void setDownloadurl(String downloadurl) {
            this.downloadurl = downloadurl;
        }

        public String getVersionnumber() {
            return versionnumber;
        }

        public void setVersionnumber(String versionnumber) {
            this.versionnumber = versionnumber;
        }

        public String getVersionname() {
            return versionname;
        }

        public void setVersionname(String versionname) {
            this.versionname = versionname;
        }
    }
}
