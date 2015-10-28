package oodesignanalysis.hiketracker;

/**
 * Created by Ryan on 10/28/15.
 */
public class UserInfo {

    /*******************  Class variables *******************/
    private String mName;
    private int mAvgHikeTime;
    private int mSummitCount;
    private String mLastPeakHiked;

    /**
     * Loads user data
     */
    public void loadUser() {

    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getAvgHikeTime() {
        return mAvgHikeTime;
    }

    public void setAvgHikeTime(int mAvgHikeTime) {
        this.mAvgHikeTime = mAvgHikeTime;
    }

    public int getSummitCount() {
        return mSummitCount;
    }

    public void setSummitCount(int mSummitCount) {
        this.mSummitCount = mSummitCount;
    }

    public String getLastPeakHiked() {
        return mLastPeakHiked;
    }

    public void setLastPeakHiked(String mLastPeakHiked) {
        this.mLastPeakHiked = mLastPeakHiked;
    }

    /**
     * Updates user info
     */
    public void updateSavedInfo(int avgHikeTime, int summitCount, String lastPeakHiked) {
        this.mAvgHikeTime = avgHikeTime;
        this.mSummitCount = summitCount;
        this.mLastPeakHiked = lastPeakHiked;
    }
}
