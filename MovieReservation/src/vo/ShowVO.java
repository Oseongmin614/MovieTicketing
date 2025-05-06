package vo;

import java.util.ArrayList;
import java.util.List;


public class ShowVO {
    
	private int showId;
    private int movieId;
    private String showTime;
    private List<ShowSeatVO> showSeatList;
    

    public ShowVO() {this.showSeatList = new ArrayList<>();}
    public ShowVO(int showId, int movieId, String showTime) {
        this.showId = showId;
        this.movieId = movieId;
        this.showTime = showTime;
    }
    
    public List<ShowSeatVO> getShowSeatList() {
        if (showSeatList == null) {
            showSeatList = new ArrayList<>();
        }
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
	
	public int getAvailableSeatsCount() {
        return (int) getShowSeatList().stream()
                .filter(seat -> !seat.isReserved())
                .count();
    }  
}
