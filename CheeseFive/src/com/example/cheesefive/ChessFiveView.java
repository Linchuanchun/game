package com.example.cheesefive;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ChessFiveView extends View implements OnTouchListener{
	int maxRows=12;
	int maxColx=10;
	int x0=10;
	int y0=100;
	int D=76;
	Bitmap start,stop;

	int chessX,chessY;
	int chessType=1;           //1表示黑子,2表示白子
    int chess[][]=new int[maxRows][maxColx];//0表示空白,1表示黑子,2表示白子
    int winner=0;
    List<StepInfo> lstStep=new ArrayList<StepInfo>();

	public ChessFiveView(Context context) {
		super(context);
	
		setOnTouchListener(this);
		start=BitmapFactory.decodeResource(getResources(),R.drawable.start1);
		stop=BitmapFactory.decodeResource(getResources(), R.drawable.stop2);
		
		
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	    
		Paint paint1=new Paint();
		paint1.setColor(Color.BLACK);
		
		
		for(int i=0;i<maxRows;i++){
			canvas.drawLine(x0, y0+i*D,x0+(maxColx-1)*D ,y0+i*D, paint1);
			
		}
		for(int i=0;i<maxColx;i++){
			canvas.drawLine(x0+i*D, y0, x0+i*D, y0+(maxRows-1)*D, paint1);
			
		}
		
		canvas.drawBitmap(stop, 500, 1000, paint1);
		paint1.setColor(Color.CYAN);
		paint1.setTextSize(50);
		canvas.drawText("我的五子棋", 250, 80, paint1);
	
		
		/*if(chessType==1){
		paint1.setColor(Color.BLACK);
		}else {
			paint1.setColor(Color.WHITE);
		}
		canvas.drawCircle(x0+chessX*D, y0+chessY*D, 20,paint1);
		//canvas.drawCircle(x0+chessX*D, y0+chessY*D, 20,paint1);
		chessType=chessType==1?2:1;
		
		*/
		for(int i=0;i<maxRows;i++){
			for(int j=0;j<maxColx;j++){
				if(chess[i][j]>0){//判断棋盘是否被占用
					if(chess[i][j]==1){
						paint1.setColor(Color.BLACK);
					}else {
						paint1.setColor(Color.WHITE);
					}
					canvas.drawCircle(x0+j*D, y0+i*D, 20,paint1);
					
					
				}
			}
		}
		
		
		if(winner>0){
			paint1.setColor(Color.RED);
			paint1.setTextSize(70);
			String s=winner==1?"黑子赢了":"白子赢了";
		    canvas.drawText(s, 100, 300, paint1);
		}
		//显示我要悔棋
		paint1.setTextSize(50);
		paint1.setColor(Color.BLUE);
		canvas.drawText("悔棋", 300, 1000, paint1);
		
		canvas.drawBitmap(start, 100, 1000, paint1);//开始游戏
		
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
	    float x=(float)event.getX();
	    float y=(float)event.getY();
	    //判断悔棋
	    if(x>=300&&x<=450&&y>=1000&&y<=1100){
	    	goBack();
	    	return false;//结束onTouch()方法的执行
	    	
	    }
	    //开始游戏
	    if(x>=100&&x<200&&y>=1000&&y<=1100){
	    	rePlay();
	    	return false;
	    	
	    }
	    
	    System.out.println(chessX);
	    System.out.println(chessY);
	    chessX=(int)((x-x0)/D+0.5);//如果超过一半,则取另一行,表示列
	    chessY=(int)((y-y0)/D+0.5);//表示行
	    //判断是否越界
	    if(chessX<0||chessX>=maxColx||chessY<0||chessY>=maxRows){
	          return false;//结束OnTouch方法的执行
	    	
	    }
	    
	   if(chess[chessY][chessX]>0){ //判断棋盘上该位置是否已经被占用
	    	return false;//结束OnTouch方法的执行
	    }
	    chess[chessY][chessX]=chessType; //用对应的棋子占据该位置
	    
	    StepInfo step=new StepInfo(chessY,chessX,chessType);
	    lstStep.add(step);
	    //更换棋子类型
	    chessType=chessType==1?2:1;
	    checkWinner();//进行胜负的判断
	    postInvalidate();  //在改变了坐标之后立即重新绘制界面
	   
	    return false;
	    
	}
	public void goBack(){
		//1.从lstStep数组中取出最上面的一个元素
		//2.获得位置行和列的信息,并将对应位置的chess[row][col]的值设为0
		//3.从新画图,把lstStep中最上面的元素去掉
		
		int n=lstStep.size();
		if(n<=0){
			return ;//退出goBack()方法的执行
			
		}
		StepInfo step=lstStep.get(n-1);
		int row=step.getRow();
		int col=step.getCol();
		chessType=step.getChessType();
		
		chess[row][col]=0;
		postInvalidate();//从新画图
		lstStep.remove(n-1);
		
	}
	public void rePlay(){
		for(int i=0;i<maxRows;i++){
			for(int j=0;j<maxColx;j++){
				chess[i][j]=0;
			}
		}
		lstStep.clear();
		chessType=1;
		winner=0;
		//从新画图
		postInvalidate();
	}
 
    
	private void checkWinner() {
		// 从上到下，从左到右 依次搜索
		for(int i=0;i<maxRows;i++)
			for(int j=0;j<maxColx;j++){
				if(chess[i][j] == 0)
				continue; //后面的语句不在执行，继续执行下一次循环
				
				//以该位置为基准，搜索4个方向 右 ，下，右上，右下
				//判断是否有五个同类型的棋子
				
			
				// 向右方向
				int count=1; 
				for(int k=1;k<=4 && (j+k<maxColx);k++){
					if(chess[i][j+k] != chess[i][j]) break;//在向右搜索的时候,如果遇到空格就不在搜索,跳出当前的循环
					else
						count++;
				}
				if(count >= 5){
					winner = chess[i][j];
					return ;//跳出checkWinner()方法的执行
					
				}
				// 向下方向
				count=1;
				for(int k=1;k<=4 && (i+k<maxRows);k++){
					if(chess[i+k][j] != chess[i][j]) break;
					else
						count++;
				}
				if(count >= 5){
					winner = chess[i][j];
					return ;//跳出checkWinner()方法的执行
					
				}
			
				//斜上
				count=1;
				for(int k=1;k<=4 && (j+k<maxColx) && (i-k>0);k++){
					if(chess[i-k][j+k] != chess[i][j]) break;
					else
						count++;
				}
				if(count >= 5){
					winner = chess[i][j];
					return ;//跳出checkWinner()方法的执行
					
				}
				
				
				
				//斜下
				count=1;
				for(int k=1;k<=4 && (j+k<maxColx) && (i+k<maxRows);k++){
					if(chess[i+k][j+k] != chess[i][j]) break;
					else
						count++;
				}
				if(count >= 5){
					winner = chess[i][j];
					return ;//跳出checkWinner()方法的执行
					
				}
				
			
				
			}
		
		
	
		
	}
}
