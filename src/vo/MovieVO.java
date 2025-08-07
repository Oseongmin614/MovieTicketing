package vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieVO {
	private int movieId;
	private String title;
	private int durationMin;
	private String description;
	private Date regDate;
	private List<ShowVO> showList;

	public MovieVO() {}

	public MovieVO(int movieId, String title, int durationMin, String description, Date regDate) {
		this.movieId = movieId;
		this.title = title;
		this.durationMin = durationMin;
		this.description = description;
		this.regDate = regDate;
	}

	public List<ShowVO> getShowList() {
		if (showList == null) {
			showList = new ArrayList<>();
		}
		return showList;
	}

	public void setShowList(List<ShowVO> showList) {
		this.showList = showList;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDurationMin() {
		return durationMin;
	}

	public void setDurationMin(int durationMin) {
		this.durationMin = durationMin;
	}
	
	public int getRunningTime() {
		return durationMin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
}