package vo;

import java.util.Date;

public class ReservationVO {
    private int reservationId;
    private int userNo;
    private int showId;
    private int seatId;
    private Date reservationDate;

    public ReservationVO() {}
    public ReservationVO(int reservationId, int userNo, int showId, int seatId, Date reservationDate) {
        this.reservationId = reservationId;
        this.userNo = userNo;
        this.showId = showId;
        this.seatId = seatId;
        this.reservationDate = reservationDate;
    }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    public int getUserNo() { return userNo; }
    public void setUserNo(int userNo) { this.userNo = userNo; }
    public int getShowId() { return showId; }
    public void setShowId(int showId) { this.showId = showId; }
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public Date getReservationDate() { return reservationDate; }
    public void setReservationDate(Date reservationDate) { this.reservationDate = reservationDate; }
}
