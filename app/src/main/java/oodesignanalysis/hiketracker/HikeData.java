package oodesignanalysis.hiketracker;

import java.util.Date;

/**
 * Created by Ryan on 10/28/15.
 */
public class HikeData {

    /*******************  Class variables *******************/
    private String peakName;
    private int hikeLength;
    private Date hikeDate;

    public HikeData(String peakName, int hikeLength, Date hikeDate) {
        this.peakName = peakName;
        this.hikeLength = hikeLength;
        this.hikeDate = hikeDate;
    }

    public String getPeakName() {
        return this.peakName;
    }

    public void setPeakName(String peakName) {
        this.peakName = peakName;
    }

    public int getHikeLength() {
        return hikeLength;
    }

    public void setHikeLength(int hikeLength) {
        this.hikeLength = hikeLength;
    }

    public Date getHikeDate() {
        return hikeDate;
    }

    public void setHikeDate(Date hikeDate) {
        this.hikeDate = hikeDate;
    }
}
