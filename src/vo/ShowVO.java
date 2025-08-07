package vo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowVO {
    private int showId;
    private int movieId;
    private Timestamp showTime;
    private List<ShowSeatVO> showSeatList;

    public ShowVO() {
        this.showSeatList = new ArrayList<>();
    }

    public int getShowId() { return showId; }
    public void setShowId(int showId) { this.showId = showId; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public Timestamp getShowTime() { return showTime; }
    public void setShowTime(Timestamp showTime) { this.showTime = showTime; }
    public List<ShowSeatVO> getShowSeatList() { return showSeatList; }
    public void setShowSeatList(List<ShowSeatVO> showSeatList) { this.showSeatList = showSeatList; }

    public String getShowDate() {
        if (this.showTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd").format(this.showTime);
    }

    public String getShowClock() {
        if (this.showTime == null) return "";
        return new SimpleDateFormat("HH:mm").format(this.showTime);
    }

    public int getAvailableSeatsCount() {
        return (int) showSeatList.stream().filter(seat -> !seat.isReserved()).count();
    }
}