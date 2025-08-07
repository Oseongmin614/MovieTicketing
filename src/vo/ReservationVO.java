package vo;

import java.util.Date;

public class ReservationVO {
    private int reservationId;
    private int userNo;
    private int showId;
    private int showSeatId;       
    private Date reservedDate;    
    private String status;        

    private String movieTitle;
    private Date showTime;
    private String seatCode;

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

    public ReservationVO(int reservationId, int userNo, int showId, int showSeatId, 
                         Date reservedDate, String status, String movieTitle, Date showTime, String seatCode) {
        this(reservationId, userNo, showId, showSeatId, reservedDate, status);
        this.movieTitle = movieTitle;
        this.showTime = showTime;
        this.seatCode = seatCode;
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
    public Date getReservationTime() {
        return reservedDate;
    }

    public void setReservationTime(Date reservationTime) {
        this.reservedDate = reservationTime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }
}
