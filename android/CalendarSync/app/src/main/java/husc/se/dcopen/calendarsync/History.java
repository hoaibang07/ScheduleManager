package husc.se.dcopen.calendarsync;

/**
 * Created by Wikoln on 12/5/2015.
 */
public class History {
    private String id;
    private java.util.Date ngayDongBo;
    private String hisName;
    private String hisContent;
    private java.util.Date hisBTime;
    private java.util.Date hisETime;
    private String hisPlace;
    private int hisType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.util.Date getNgayDongBo() {
        return ngayDongBo;
    }

    public void setNgayDongBo(java.util.Date ngayDongBo) {
        this.ngayDongBo = ngayDongBo;
    }

    public String getHisName() {
        return hisName;
    }

    public void setHisName(String hisName) {
        this.hisName = hisName;
    }

    public String getHisContent() {
        return hisContent;
    }

    public void setHisContent(String hisContent) {
        this.hisContent = hisContent;
    }

    public java.util.Date getHisBTime() {
        return hisBTime;
    }

    public void setHisBTime(java.util.Date hisBTime) {
        this.hisBTime = hisBTime;
    }

    public java.util.Date getHisETime() {
        return hisETime;
    }

    public void setHisETime(java.util.Date hisETime) {
        this.hisETime = hisETime;
    }

    public String getHisPlace() {
        return hisPlace;
    }

    public void setHisPlace(String hisPlace) {
        this.hisPlace = hisPlace;
    }

    public int getHisType() {
        return hisType;
    }

    public void setHisType(int hisType) {
        this.hisType = hisType;
    }
}
