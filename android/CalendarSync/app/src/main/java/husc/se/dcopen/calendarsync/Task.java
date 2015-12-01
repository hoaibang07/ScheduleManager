package husc.se.dcopen.calendarsync;

public class Task {
    private String id;
    private String taskName;
    private java.sql.Date beginTime;
    private java.sql.Date endTime;
    private String place;
    private String taskContent;
    private int type;
    private String accountName;
    private int sync;

    public Task() {}

    public Task(String id, String taskName, java.sql.Date beginTime,java.sql.Date endTime,
                String place, String taskContent, String accountName, int type, int sync) {
        this.id = id;
        this.taskName = taskName;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.place = place;
        this.taskContent = taskContent;
        this.accountName = accountName;
        this.type = type;
        this.sync = sync;
    }

    public String getId() {
        return id;
    }

    public Task setId(String id) {
        this.id = id;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public Task setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public java.sql.Date getBeginTime() {
        return beginTime;
    }

    public Task setBeginTime(java.sql.Date beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public java.sql.Date getEndTime() {
        return endTime;
    }

    public Task setEndTime(java.sql.Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getPlace() {
        return place;
    }

    public Task setPlace(String place) {
        this.place = place;
        return this;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public Task setTaskContent(String taskContent) {
        this.taskContent = taskContent;
        return this;
    }

    public int getType() {
        return type;
    }

    public Task setType(int type) {
        this.type = type;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public Task setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public int getSync() {
        return sync;
    }

    public Task setSync(int sync) {
        this.sync = sync;
        return this;
    }
}
