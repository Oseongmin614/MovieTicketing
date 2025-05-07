package vo;

import java.util.Date;

public class ReservationVO {
    private int reservationId;
    private int userNo;
    private int showId;
    private int showSeatId;       
    private Date reservedDate;    
    private String status;        

    public ReservationVO() {}

    public ReservationVO(int reservationId, int userNo, int showId, int showSeatId, 
                         Date reservedDate, String status) {
        this.reservationId = reservationId;
        this.userNo = userNo;
        this.showId = showId;
        this.showSeatId = showSeatId;
        this.reservedDate = reservedDate;
        this.status = status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getShowSeatId() {
        return showSeatId;
    }

    public void setShowSeatId(int showSeatId) {
        this.showSeatId = showSeatId;
    }

    public Date getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(Date reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
