package vo;

import java.util.ArrayList;
import java.util.List;

public class ShowVO {
    private int showId;
    private int movieId;
    private String showTime; // DB SHOW_TIME이 DATE라면 Date 타입 사용 권장
    private List<ShowSeatVO> showSeatList;

    public ShowVO() {
        this.showSeatList = new ArrayList<>();
    }

    public ShowVO(int showId, int movieId, String showTime) {
        this.showId = showId;
        this.movieId = movieId;
        this.showTime = showTime;
        this.showSeatList = new ArrayList<>();
    }

    public List<ShowSeatVO> getShowSeatList() {
        return showSeatList;
    }

    public void setShowSeatList(List<ShowSeatVO> showSeatList) {
        this.showSeatList = showSeatList;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    // 남은 좌석 수 계산
    public int getAvailableSeatsCount() {
        return (int) showSeatList.stream().filter(seat -> !seat.isReserved()).count();
    }

  
}
