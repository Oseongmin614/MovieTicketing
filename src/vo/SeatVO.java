package vo;

public class SeatVO {
	private int seatId;
	private String rowLabel;
	private int colNum;
	private String seatCode;

	public SeatVO() {}

	public SeatVO(int seatId, String rowLabel, int colNum, String seatCode) {
		this.seatId = seatId;
		this.rowLabel = rowLabel;
		this.colNum = colNum;
		this.seatCode = seatCode;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
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