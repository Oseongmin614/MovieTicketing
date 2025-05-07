package vo;

public class ShowSeatVO {
	private int showSeatId;
	private int showId;
	private int seatId;
	private boolean isReserved;

	public ShowSeatVO() {}

	public ShowSeatVO(int showSeatId, int showId, int seatId, boolean isReserved) {
		this.showSeatId = showSeatId;
		this.showId = showId;
		this.seatId = seatId;
		this.isReserved = isReserved;
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

}
