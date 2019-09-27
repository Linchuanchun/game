package com.example.cheesefive;

public class StepInfo {
	int row;
	int col;
	int chessType;
	
	
	
	public StepInfo(int row, int col, int chessType) {
		super();
		this.row = row;
		this.col = col;
		this.chessType = chessType;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getChessType() {
		return chessType;
	}
	public void setChessType(int chessType) {
		this.chessType = chessType;
	}
	
	
	
	
	

}
