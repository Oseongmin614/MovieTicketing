package vo;

public class ShowSeatVO {
    private int showSeatId;
    private int showId;
    private int seatId;
    private boolean isReserved;
    private String rowLabel;
    private int colNum;
    private String seatCode;
    
  
    public ShowSeatVO() {
    }
    
 
    public ShowSeatVO(int showSeatId, int showId, int seatId, boolean isReserved,
                     String rowLabel, int colNum, String seatCode) {
        this.showSeatId = showSeatId;
        this.showId = showId;
        this.seatId = seatId;
        this.isReserved = isReserved;
        this.rowLabel = rowLabel;
        this.colNum = colNum;
        this.seatCode = seatCode;
    }
    
    public int getShowSeatId() {
        return showSeatId;
    }
    
    public void setShowSeatId(int showSeatId) {
        this.showSeatId = showSeatId;
    }
    
    public int getShowId() {
        return showId;
    }
    
    public void setShowId(int showId) {
        this.showId = showId;
    }
    
    public int getSeatId() {
        return seatId;
    }
    
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
    
    public boolean isReserved() {
        return isReserved;
    }
    
    public void setReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }
    
    public String getRowLabel() {
        return rowLabel;
    }
    
    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }
    
    public int getColNum() {
        return colNum;
    }
    
    public void setColNum(int colNum) {
        this.colNum = colNum;
    }
    
    public String getSeatCode() {
        return seatCode;
    }
    
    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }
}