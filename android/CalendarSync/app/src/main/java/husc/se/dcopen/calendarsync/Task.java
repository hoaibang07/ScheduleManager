package husc.se.dcopen.calendarsync;

public class Task {
    private String id;
    private String taskName;
    private java.util.Date beginTime;
    private java.util.Date endTime;
    private String place;
    private String taskContent;
    private int type;
    private String accountName;
    private int sync;

    public Task() {}

    public Task(String id, String taskName, java.util.Date beginTime,java.util.Date endTime,
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

    public java.util.Date getBeginTime() {
        return beginTime;
    }

    public Task setBeginTime(java.util.Date beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public java.util.Date getEndTime() {
        return endTime;
    }

    public Task setEndTime(java.util.Date endTime) {
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
