package com.my.chess;

import java.util.Stack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ChessView  extends View{	

	    public  class TreeNode implements Cloneable{
	        public int credits;
	        public char move;
	        public boolean selected;
	        public ArrayList<TreeNode> children;
	        public int minchild;
	        public int maxchild;
	        public TreeNode parent;
	        public TreeNode(int c,char m) {
	            this.credits = c;
	            this.move=m;
	            this.parent=null;
	            this.children = null;
	            this.selected=false;
	        }
	        public TreeNode() {
	            this.credits = 0;
	            this.move=0;
	            this.children = null;
	            this.parent=null;
	            this.selected=false;
	        }
	        public TreeNode addChild(int c,char m) {
	            TreeNode childNode = new TreeNode(c,m);
	            childNode.parent = this;
	            if(this.children==null)
	            {
	            	this.children=new ArrayList<TreeNode>();
	            }
	            this.children.add(childNode);
	            return childNode;
	        }

	    }

    public static ChessView cv; 
	private Context mContext;
	private Resources res;
	private Bitmap mBgImage;
	private TextView tv;
	 
	public int myColor=0;
	public static final int chess_rgiraffe=7;
    public static final int chess_rlion=9;
    public static final int chess_relephant=6;
    public static final int chess_rchick=5;
    public static final int chess_chick=0;
    public static final int chess_elephant=1;
    public static final int chess_lion=4;
    public static final int chess_giraffe=2;
    public static final int chess_turkey=3;
    public static final int chess_rturkey=8;
    public static final int chess_nochess=10;
	
	public int index_clicktime=0;
	public int selected_chesscol=-1;
	public int selected_chessrow=-1;
	public Stack<int[][]> gamestack=new Stack<int[][]>();
	public Stack<int[]> bonus=new Stack<int[]>();
	public boolean enable=false;
	
	/////////////////////////////
	int[][][] view_coor_chessboard={{{47,50},{132,50},{218,50}},{{47,114},{132,114},{218,114}},{{47,178},{132,178},{218,178}},{{47,242},{132,242},{218,242}},{{47,306},{132,306},{218,306}},{{47,370},{132,370},{218,370}}};
	int[][]   logic_coor_chessboard={{chess_nochess,chess_nochess,chess_nochess},{chess_rgiraffe,chess_rlion,chess_relephant},{chess_nochess,chess_rchick,chess_nochess},{chess_nochess,chess_chick,chess_nochess},{chess_elephant,chess_lion,chess_giraffe},{chess_nochess,chess_nochess,chess_nochess}};
	int[] bonuschess_num={0,0,0,0,0,0};
	int[][][] available={{{0,1,0},{0,0,0},{0,0,0}},{{1,0,1},{0,0,0},{1,0,1}},{{0,1,0},{1,0,1},{0,1,0}},{{1,1,1},{1,0,1},{0,1,0}},{{1,1,1},{1,0,1},{1,1,1}}};
	int[][][] ravailable={{{0,0,0},{0,0,0},{0,1,0}},{{1,0,1},{0,0,0},{1,0,1}},{{0,1,0},{1,0,1},{0,1,0}},{{0,1,0},{1,0,1},{1,1,1}},{{1,1,1},{1,0,1},{1,1,1}}};
	private boolean isComputer=false;
	Paint paint=new Paint();
	Paint paint1=new Paint();
	///////////////////////////////
	
	Bitmap[] chessImage=new Bitmap[10];

	public TextView getTv() {
		return tv;
	}

	public void setTv(TextView tv) {
		this.tv = tv;
	}
	
	public boolean CheckMate(int x,int y)
	{
		if(logic_coor_chessboard[y][x]!=chess_lion&&logic_coor_chessboard[y][x]!=chess_rlion)
			return false;
		else if(logic_coor_chessboard[y][x]==chess_lion)
		{
			for(int i=-1;i<2;i++)
			{
				for(int j=-1;j<2;j++)
				{
				  if((x+j)>=0&&(x+j)<=2&&(y+i)>=1&&(y+i)<=4)
				  {
					 if(logic_coor_chessboard[y+i][x+j]==chess_rlion)
					 {
						 return true;
					 }
					 if(Math.abs(i)==1&&Math.abs(j)==1)
					 {
						 if(logic_coor_chessboard[y+i][x+j]==chess_relephant)
						 {
						     return true;
						 }
					 }
				     if((Math.abs(i)==0&&Math.abs(j)==1)||(Math.abs(i)==1&&Math.abs(j)==0))
					 {
						 if(logic_coor_chessboard[y+i][x+j]==chess_rgiraffe||logic_coor_chessboard[y+i][x+j]==chess_rturkey)
						 {
							 return true;
						 }
					 }
				     if(i==-1&&j==0)
				     {
				    	 if(logic_coor_chessboard[y+i][x+j]==chess_rchick)
				    	 {
				    		 return true;
				    	 }
				     }
				     if(i==-1&&(j==-1||j==1))
				     {
				    	 if(logic_coor_chessboard[y+i][x+j]==chess_rturkey)
				    	 {
				    		 return true;
				    	 }
				     }
				  }
				}
			}
			return false;
		}
		else if(logic_coor_chessboard[y][x]==chess_rlion)
		{
			for(int i=-1;i<2;i++)
			{
				for(int j=-1;j<2;j++)
				{
				  if((x+j)>=0&&(x+j)<=2&&(y+i)>=1&&(y+i)<=4)
				  {
					 if(logic_coor_chessboard[y+i][x+j]==chess_lion)
					 {
						 return true;
					 }
					 if(Math.abs(i)==1&&Math.abs(j)==1)
					 {
						 if(logic_coor_chessboard[y+i][x+j]==chess_elephant)
						 {
						     return true;
						 }
					 }
				     if((Math.abs(i)==0&&Math.abs(j)==1)||(Math.abs(i)==1&&Math.abs(j)==0))
					 {
						 if(logic_coor_chessboard[y+i][x+j]==chess_giraffe||logic_coor_chessboard[y+i][x+j]==chess_turkey)
						 {
							 return true;
						 }
					 }
				     if(i==1&&j==0)
				     {
				    	 if(logic_coor_chessboard[y+i][x+j]==chess_chick)
				    	 {
				    		 return true;
				    	 }
				     }
				     if(i==1&&(j==-1||j==1))
				     {
				    	 if(logic_coor_chessboard[y+i][x+j]==chess_turkey)
				    	 {
				    		 return true;
				    	 }
				     }
				  }
				}
		   }
		   return false;
		}
		return false;
	}
	public int CheckWin()
	{
		int mlion=0;
		int rlion=0;
		 for(int i=0;i<3;i++) 
		 {
		   for(int j=1;j<=4;j++)
		   {
			  if(logic_coor_chessboard[j][i]==chess_lion)
			  {
				  mlion++;
			  }
			  if(logic_coor_chessboard[j][i]==chess_rlion)
			  {
				  rlion++;
			  }
		   } 
		 }
		 if(mlion==0)
			 return 2;
		 if(rlion==0)
			 return 1;
		 int lioncol=-1;
		 for(int i=0;i<3;i++)
		 {
			 if(logic_coor_chessboard[1][i]==chess_lion)
				 lioncol=i;
		 }
		 if(lioncol!=-1)
		 {
			 if(CheckMate(lioncol,1)==false)
				 return 1;
		 }
		 lioncol=-1;
		 for(int i=0;i<3;i++)
		 {
			 if(logic_coor_chessboard[4][i]==chess_rlion)
				 lioncol=i;
		 }
		 if(lioncol!=-1)
		 {
			 if(CheckMate(lioncol,4)==false)
				 return 2;
		 }
		 return 0;
		 
	}
	public ChessView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ChessView(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		cv=this;
		String con=context.toString();
		Log.i("context",con);
		mContext=context;
		res=context.getResources();
		paint.setARGB(100, 0, 255, 0);
		paint1.setARGB(100, 0, 0, 255);
		mBgImage=BitmapFactory.decodeResource(res,R.drawable.chessboard);
		chessImage[chess_lion]=BitmapFactory.decodeResource(res,R.drawable.lion);
		chessImage[chess_rlion]=BitmapFactory.decodeResource(res,R.drawable.rlion);
		chessImage[chess_elephant]=BitmapFactory.decodeResource(res,R.drawable.elphant);
		chessImage[chess_relephant]=BitmapFactory.decodeResource(res,R.drawable.relephant);
		chessImage[chess_giraffe]=BitmapFactory.decodeResource(res,R.drawable.giraffe);
		chessImage[chess_rgiraffe]=BitmapFactory.decodeResource(res,R.drawable.rgiraffe);
		chessImage[chess_chick]=BitmapFactory.decodeResource(res,R.drawable.chick);
		chessImage[chess_rchick]=BitmapFactory.decodeResource(res,R.drawable.rchick);
		chessImage[chess_turkey]=BitmapFactory.decodeResource(res,R.drawable.turkey);
		chessImage[chess_rturkey]=BitmapFactory.decodeResource(res,R.drawable.rturkey);		
		setFocusable(true);
		
	}	
	
	
	public void initGame(){	
	  logic_coor_chessboard[0][0]=chess_nochess;
	  logic_coor_chessboard[0][1]=chess_nochess;
	  logic_coor_chessboard[0][2]=chess_nochess;
	  logic_coor_chessboard[1][0]=chess_rgiraffe;
	  logic_coor_chessboard[1][1]=chess_rlion;
	  logic_coor_chessboard[1][2]=chess_relephant;
	  logic_coor_chessboard[2][0]=chess_nochess;
	  logic_coor_chessboard[2][1]=chess_rchick;
	  logic_coor_chessboard[2][2]=chess_nochess;
	  logic_coor_chessboard[3][0]=chess_nochess;
	  logic_coor_chessboard[3][1]=chess_chick;
	  logic_coor_chessboard[3][2]=chess_nochess;
	  logic_coor_chessboard[4][0]=chess_elephant;
	  logic_coor_chessboard[4][1]=chess_lion;
	  logic_coor_chessboard[4][2]=chess_giraffe;
	  logic_coor_chessboard[5][0]=chess_nochess;
	  logic_coor_chessboard[5][1]=chess_nochess;
	  logic_coor_chessboard[5][2]=chess_nochess;	
	  bonuschess_num[0]=0;
	  bonuschess_num[1]=0;
	  bonuschess_num[2]=0;
	  bonuschess_num[3]=0;
	  bonuschess_num[4]=0;
	  bonuschess_num[5]=0;
	  selected_chessrow=-1;
	  selected_chesscol=-1;
	  gamestack.clear();
	  bonus.clear();
	  saveChess();
      invalidate();
      //  initPoint();
	}
	 private void showWinDialog(String title,String message){
	    	new AlertDialog.Builder(this.mContext)
	    	.setTitle(title)
	    	.setMessage(message)
	    	.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					
				}
	    		
	    	}).show();
	    }
	public void Win(int i)
	{
	  if(i==1)
	  {
		  showWinDialog("Message","You Win");
		  initGame();
	  }
	  else if(i==2)
	  {
		  showWinDialog("Message","You Lose");
		  initGame();
	  }
	}
	public ChessView(Context context) {
		super(context);	
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		super.draw(canvas);
		canvas.drawBitmap(mBgImage,0,0, null);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLUE);
		paint.setAlpha(127);
		paint1.setStyle(Style.FILL);
		paint1.setColor(Color.RED);
		paint1.setAlpha(127);
		//canvas.drawTextOnPath(text, path, hOffset, vOffset, paint);
		for(int row=0;row<6;row++)
		{
			for(int col=0;col<3;col++)
			{
				if(logic_coor_chessboard[row][col]!=chess_nochess)
				canvas.drawBitmap(chessImage[logic_coor_chessboard[row][col]],view_coor_chessboard[row][col][0],view_coor_chessboard[row][col][1],null);
			}			
		}
			if(selected_chessrow!=-1&&selected_chesscol!=-1)
			{ 
				int tempx=view_coor_chessboard[selected_chessrow][selected_chesscol][0];
				int tempy=view_coor_chessboard[selected_chessrow][selected_chesscol][1];
				int temptype=logic_coor_chessboard[selected_chessrow][selected_chesscol];
				int ttx=0;
				int tty=0;
				canvas.drawRect(tempx-15,tempy-3,tempx+70,tempy+55,paint1);
				if(selected_chessrow!=5)//paint available moves for player
				{
					for(int i=0;i<3;i++)
					{
						for(int j=0;j<3;j++)
						{
							if(available[temptype%5][i][j]==1)//check available
							{
								if((selected_chessrow+i-1)>=1&&(selected_chessrow+i-1)<=4&&(selected_chesscol+j-1)>=0&&(selected_chesscol+j-1)<=2) // check range
								{
									if(logic_coor_chessboard[selected_chessrow+i-1][selected_chesscol+j-1]/5==0)
										continue;
									ttx=view_coor_chessboard[selected_chessrow+i-1][selected_chesscol+j-1][0];
									tty=view_coor_chessboard[selected_chessrow+i-1][selected_chesscol+j-1][1];
									canvas.drawRect(ttx-15,tty-3,ttx+70,tty+55,paint);
								}
							}
						}
					}
				}         	
			}
	}
	public void recoverChess()
	{
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<3;j++)
			{
				logic_coor_chessboard[i][j]=gamestack.lastElement()[i][j];
			}
		}
		for(int i=0;i<6;i++)
		{
			bonuschess_num[i]=bonus.lastElement()[i];
		}
	}
	public void saveChess()
	{
		int[][] temp=new int[6][3];
	    for(int i=0;i<6;i++)
	    {
	    	for(int j=0;j<3;j++)
	    	{
	    		temp[i][j]=logic_coor_chessboard[i][j];
	    	}
	    }
	    gamestack.add(temp);
	    int[] b=new int[6];
	    for(int i=0;i<6;i++)
	    {
	    	b[i]=bonuschess_num[i];
	    }
	    bonus.add(b);
	}
	 public int CalCredits() //White is at the lower side, Black is at the upper side
	 {
		 int credit=0;
		 int temptype=-1;
		 if(CheckWin()==1)
			 return 100;
		 if(CheckWin()==2)
			 return -100;
		 for(int i=0;i<3;i++)
		 {
			if(logic_coor_chessboard[0][i]/5==1)
			{
				 credit = credit-logic_coor_chessboard[0][i]-1;
			}
			if(logic_coor_chessboard[5][i]/5==0)
			 {
				 credit = credit+logic_coor_chessboard[5][i]+5+1;
			 }
	    }
		for(int i=0;i<3;i++)
		{
			 for(int j=1;j<=4;j++)
			 {
				 temptype=logic_coor_chessboard[j][i];
				 if(temptype/5==0)
				 {
					 credit=credit+temptype+5;
					 for(int mi=0;mi<3;mi++)
						{
							for(int mj=0;mj<3;mj++)
							{
								if(available[temptype%5][mj][mi]==1)//check available
								{
									if((j+mj-1)>=1&&(j+mj-1)<=4&&(i+mi-1)>=0&&(i+mi-1)<=2) // check range
									{
										if(logic_coor_chessboard[j+mj-1][i+mi-1]==chess_nochess)
										{
											credit+=1;
										}		
									}
								}
							}
						}
				 }
			 }
		 }
		
		 return credit;
	 }
	 public void GenerateTree(TreeNode gametree,int level)
	 {
		 if(level==5)
			 return ;

		 int temptype=-1;
		 if(level%2==0)
		 {
		  for(int i=0;i<3;i++)
		  {
			  if(logic_coor_chessboard[0][i]!=chess_nochess)
			  {
				  for(int mi=0;mi<3;mi++)
				  {
					  for(int mj=2;mj<=3;mj++)
					  {
						  if(logic_coor_chessboard[mj][mi]==chess_nochess)
						  {
							    char c= 0;
								c = (char)(c|i);
								c = (char) (c<<3);
								c = (char)(c |0);
								c = (char) (c<<2);
								c = (char)((mi)|(c));
								c = (char)(c<<3);
								c = (char)((mj)|(c));
								movechess(c);
								saveChess();
								int credit=CalCredits();
								GenerateTree(gametree.addChild(credit, c),level+1);	
								gamestack.pop();
								bonus.pop();
								recoverChess();
						  }
					  }
				  }
			  }
		  }
		  for(int i=0;i<3;i++)
		  {
			 for(int j=1;j<=4;j++)
			 {
				 temptype=logic_coor_chessboard[j][i];
				 if(temptype/5==1)
				 {	 
					 for(int mi=0;mi<3;mi++)
						{
							for(int mj=0;mj<3;mj++)
							{
								if(ravailable[temptype%5][mj][mi]==1)//check available
								{
									if((j+mj-1)>=1&&(j+mj-1)<=4&&(i+mi-1)>=0&&(i+mi-1)<=2) // check range
									{
										if(logic_coor_chessboard[j+mj-1][i+mi-1]/5==1)
											continue;
										char c= 0;
										c = (char)(c|i);
										c = (char) (c<<3);
										c = (char)(c |(j));
										c = (char) (c<<2);
										c = (char)((i+mi-1)|(c));
										c = (char)(c<<3);
										c = (char)((j+mj-1)|(c));
										movechess(c);
										saveChess();
										int credit=CalCredits();
										GenerateTree(gametree.addChild(credit, c),level+1);	
										gamestack.pop();
										bonus.pop();
										recoverChess();						
									}
								}
							}
						}
					}		
			 }
		  }
		 }
		 else if(level%2==1)
		 {
			 for(int i=0;i<3;i++)
			  {
				  if(logic_coor_chessboard[5][i]!=chess_nochess)
				  {
					  for(int mi=0;mi<3;mi++)
					  {
						  for(int mj=2;mj<=3;mj++)
						  {
							  if(logic_coor_chessboard[mj][mi]==chess_nochess)
							  {
								    char c= 0;
									c = (char)(c|i);
									c = (char) (c<<3);
									c = (char)(c |5);
									c = (char) (c<<2);
									c = (char)((mi)|(c));
									c = (char)(c<<3);
									c = (char)((mj)|(c));
									movechess(c);
									saveChess();
									int credit=CalCredits();
									GenerateTree(gametree.addChild(credit, c),level+1);	
									gamestack.pop();
									bonus.pop();
									recoverChess();									
							  }
						  }
					  }
				  }
			  }
		  for(int i=0;i<3;i++)
		  {
			 for(int j=1;j<=4;j++)
			 {
				 temptype=logic_coor_chessboard[j][i];
				 if(temptype/5==0)
				 {	 
					 for(int mi=0;mi<3;mi++)
						{
							for(int mj=0;mj<3;mj++)
							{
								if(available[temptype%5][mj][mi]==1)//check available
									{
										if((j+mj-1)>=1&&(j+mj-1)<=4&&(i+mi-1)>=0&&(i+mi-1)<=2) // check range
										{
											if(logic_coor_chessboard[j+mj-1][i+mi-1]/5==0)
												continue;
											char c= 0;
											c = (char)(c|i);
											c = (char) (c<<3);
											c = (char)(c |(j));
											c = (char) (c<<2);
											c = (char)((i+mi-1)|(c));
											c = (char)(c<<3);
											c = (char)((j+mj-1)|(c));
											movechess(c);
											saveChess();
											int credit=CalCredits();
											GenerateTree(gametree.addChild(credit, c),level+1);	
											gamestack.pop();
											bonus.pop();	
											recoverChess();														
										}
									}
							}
						}
				}		
			 }
		  }
		 }
		 return ;
	  }
	 public void MinMax(TreeNode gametree)
	 {
		 int i=0;
		 int indmax=0;
		 int indmin=0;
		 int tempmax=-1000;
		 int tempmin=1000;
		 for(TreeNode p:gametree.children)
		 {
			 if(p.credits>tempmax)
			 {
				 indmax=i;
				 tempmax=p.credits;
			 }
			 if(p.credits<tempmin)
			 {
				 indmin=i;
				 tempmin=p.credits;
			 }
			 if(p.children!=null)
			 {
				 MinMax(p);
			 }
			 i++;
		 }
		 gametree.minchild=indmin;
		 gametree.maxchild=indmax;
	 }
	 public int Route(TreeNode gametree)
	 {	
		 int indmin=0;
		 int tempmin=1000;
		 int i=0;
		 TreeNode lvl1;
		 TreeNode lvl2;
		 TreeNode lvl3;
		 TreeNode lvl4;
		 for(TreeNode p:gametree.children)
		 {
			 if(p.selected==true)
			 {
				 i++;
				 continue;
			 }
		    lvl1 = p.children.get(p.maxchild);
		    lvl2 = lvl1.children.get(lvl1.minchild);
		    lvl3 = lvl2.children.get(lvl2.maxchild);
		    lvl4 = lvl3.children.get(lvl3.minchild);
		    if(lvl4.credits<tempmin)
		    {
		    	indmin=i;
		    	tempmin=lvl4.credits;
		    }
		    i++;
		 }
		return indmin;
	 }
	 public boolean MlionIsChecked()
	 {
		 boolean checked=false;
		 for(int i=0;i<3;i++)
		  {
			 for(int j=1;j<=4;j++)
			 {
			   if(logic_coor_chessboard[j][i]==chess_rlion)
			   {
				   checked=CheckMate(i,j);
			   }
			 }
		  }
		 return checked;
	 }
	 public void computerMoveChess(){
		 TreeNode gametree=new TreeNode(0,(char)0);
		 GenerateTree(gametree,0);		
		 MinMax(gametree);
		 int ind=Route(gametree);
		 
		 for(TreeNode p:gametree.children)
		 {
			if(p.credits==-100)
			{
				movechess(p.move);
				return ;
			}
		 }
		 movechess(gametree.children.get(ind).move);
		 while(MlionIsChecked())
		 {
			 gametree.children.get(ind).selected=true;
			 recoverChess();
			 ind=Route(gametree);
			 movechess(gametree.children.get(ind).move);
			 
		 }
		
	}	
	public int XconvertViewToLogic(int num)
	{
		return (num-32)/85;
	}
	public int YconvertViewToLogic(int num)
	{
		return (num-48)/64;
	}
	public boolean movechess(char c)
	{
		int startx=0;
		int starty=0;
		int endx=0;
		int endy=0;
		endy=c&7;
	    c = (char)(c>>3);
	    endx=c&3;
	    c =(char)(c>>2);
	    starty=c&7;
	    c = (char)(c>>3);
	    startx=c&3;
	    return movechess(starty,startx,endy,endx);
	}
	/*chess`s moving rule */
	//return true means game is over and player win
	//return false means game is going on
	//code before this has already guaranteed that startrow is from 1-5 and endrow is from 1-4  and the new selected chess is nochess or computer chess
	public boolean movechess(int startrow,int startcol,int endrow,int endcol)
	{
      int chess_stype=logic_coor_chessboard[startrow][startcol];
      int chess_etype=logic_coor_chessboard[endrow][endcol];
      if(startrow==5||startrow==0) //put a bonus chess into chessboard
  	  {
  		if(chess_etype!=chess_nochess) //the target chessboard has chess
  			return false;
  	    if(chess_stype>=0&&chess_stype<=2)
  		{
  			bonuschess_num[chess_stype+3]--;
  			if(bonuschess_num[chess_stype+3]==0)
  			{
  				logic_coor_chessboard[startrow][startcol]=chess_nochess;
  			}
  				logic_coor_chessboard[endrow][endcol]=chess_stype;
  		}
  	   else if(chess_stype>=5&&chess_stype<=7)
  	   {
  		   bonuschess_num[chess_stype-5]--;
  		   if(bonuschess_num[chess_stype-5]==0)
  		   {
  			   logic_coor_chessboard[startrow][startcol]=chess_nochess;
  		   }
  		   	   logic_coor_chessboard[endrow][endcol]=chess_stype;
       }
	   return true;
  	  }  
      switch(chess_stype)
      {
        case chess_chick: //chick`s moving rule
        		if(startrow-endrow==1&&startcol==endcol)
        		{
        			if(endrow==1) //chick change into turkey when it`s arriving a final line
        			{
        				logic_coor_chessboard[endrow][endcol]=chess_turkey;
        				logic_coor_chessboard[startrow][startcol]=chess_nochess;
        			}
        			else
        			{
        				logic_coor_chessboard[endrow][endcol]=chess_chick;
        				logic_coor_chessboard[startrow][startcol]=chess_nochess;
        			}
        			if(chess_etype>=5&&chess_etype<=7)
        			{
        			  logic_coor_chessboard[5][chess_etype-5]=chess_etype-5;
        			  bonuschess_num[chess_etype-2]++;
        			}		
        			if(chess_etype==8)
        			{
        				logic_coor_chessboard[5][0]=chess_chick;
        				bonuschess_num[3]++;
        			}
        		}
        		else
        		{
        			return false;
        		}
        	break;
        case chess_rchick:
        	if(endrow-startrow==1&&startcol==endcol)
    		{
               if(endrow==4) //chick change into turkey when it`s arriving a final line
        	   {
        			logic_coor_chessboard[endrow][endcol]=chess_rturkey;
        			logic_coor_chessboard[startrow][startcol]=chess_nochess;
        		}
        		else
        		{
        			logic_coor_chessboard[endrow][endcol]=chess_rchick;
        			logic_coor_chessboard[startrow][startcol]=chess_nochess;
        		}
        		if(chess_etype>=0&&chess_etype<=2)
        		{
        			logic_coor_chessboard[0][chess_etype]=chess_etype+5;
        			bonuschess_num[chess_etype]++;
        		}	
        		if(chess_etype==3)
    			{
    				logic_coor_chessboard[0][0]=chess_rchick;
    				bonuschess_num[0]++;
    			}
        	}
        	else
        	{
        		return false;
        	}
        	break;
        case chess_elephant://elephant`s moving rule
        		if(Math.abs(startrow-endrow)==1&&Math.abs(startcol-endcol)==1)
        		{
        		  //elephant`s normal move
        			logic_coor_chessboard[endrow][endcol]=chess_stype;
        			logic_coor_chessboard[startrow][startcol]=chess_nochess;
        			if(chess_etype>=5&&chess_etype<=7)
        			{
        				logic_coor_chessboard[5][chess_etype-5]=chess_etype-5;
        				bonuschess_num[chess_etype-2]++;
        			}		
        			if(chess_etype==8)
        			{
        				logic_coor_chessboard[5][0]=chess_chick;
        				bonuschess_num[3]++;
        			}
        		}
        		else
        		{
        			return false;
        		}
        	    break;
        case chess_relephant:
        	if(Math.abs(startrow-endrow)==1&&Math.abs(startcol-endcol)==1)
    		{
    	      //elephant`s normal move
    			logic_coor_chessboard[endrow][endcol]=chess_stype;
    			logic_coor_chessboard[startrow][startcol]=chess_nochess;
    			if(chess_etype>=0&&chess_etype<=2)
    			{
    				logic_coor_chessboard[0][chess_etype]=chess_etype+5;
    				bonuschess_num[chess_etype]++;
    			}	
    			if(chess_etype==3)
    			{
    				logic_coor_chessboard[0][0]=chess_rchick;
    				bonuschess_num[0]++;
    			}
    	 	}
        	else
        	{
        		return false;
        	}
         	break;
        case chess_giraffe://giraffe`s moving rule
        	if((Math.abs(startrow-endrow)==1&&startcol==endcol)||(startrow==endrow&&Math.abs(startcol-endcol)==1))
    		{
    			logic_coor_chessboard[endrow][endcol]=chess_stype;
    			logic_coor_chessboard[startrow][startcol]=chess_nochess;
    			if(chess_etype>=5&&chess_etype<=7)
    			{
    				logic_coor_chessboard[5][chess_etype-5]=chess_etype-5;
    				bonuschess_num[chess_etype-2]++;
    			}			
    			if(chess_etype==8)
    			{
    				logic_coor_chessboard[5][0]=chess_chick;
    				bonuschess_num[3]++;
    			}
    		}
        	else 
        		return false;
        	break; 
        case chess_rgiraffe:
        	if((Math.abs(startrow-endrow)==1&&startcol==endcol)||(startrow==endrow&&Math.abs(startcol-endcol)==1))
    		{
    			logic_coor_chessboard[endrow][endcol]=chess_stype;
    			logic_coor_chessboard[startrow][startcol]=chess_nochess;
    			if(chess_etype>=0&&chess_etype<=2)
    			{
    				logic_coor_chessboard[0][chess_etype]=chess_etype+5;
    				bonuschess_num[chess_etype]++;
    			}
    			if(chess_etype==3)
    			{
    				logic_coor_chessboard[0][0]=chess_rchick;
    				bonuschess_num[0]++;
    			}
    		}
        	else 
        		return false;
        	break;
        case chess_turkey:
        	if(Math.abs(startrow-endrow)<=1&&Math.abs(startcol-endcol)<=1)
        	{
        		if(startrow<endrow&&startcol!=endcol)
        		{
        			break;
        		}
        		else //giraffe`s normal move
        		{
        			logic_coor_chessboard[endrow][endcol]=chess_stype;
        			logic_coor_chessboard[startrow][startcol]=chess_nochess;
        			if(chess_etype>=5&&chess_etype<=7)
        			{
        				logic_coor_chessboard[5][chess_etype-5]=chess_etype-5;
        				bonuschess_num[chess_etype-2]++;
        			}
        			if(chess_etype==8)
        			{
        				logic_coor_chessboard[5][0]=chess_chick;
        				bonuschess_num[3]++;
        			}
        		}
        	}
        	else 
        		return false;
        	break;
        case chess_rturkey:
        	if(Math.abs(startrow-endrow)<=1&&Math.abs(startcol-endcol)<=1)
        	{
        		if(startrow>endrow&&startcol!=endcol)
        		{
        				break;
        		}
        		else //giraffe`s normal move
        		{
        			logic_coor_chessboard[endrow][endcol]=chess_stype;
        			logic_coor_chessboard[startrow][startcol]=chess_nochess;
        			if(chess_etype>=0&&chess_etype<=2)
        			{
        				logic_coor_chessboard[0][chess_etype]=chess_etype+5;
        				bonuschess_num[chess_etype]++;
        			}
        			if(chess_etype==3)
        			{
        				logic_coor_chessboard[0][0]=chess_rchick;
        				bonuschess_num[0]++;
        			}
        		}
        	}
        	else 
        		return false;
        	break;
        case chess_lion:
        	if(Math.abs(startrow-endrow)<=1&&Math.abs(startcol-endcol)<=1)
        	{
        		logic_coor_chessboard[endrow][endcol]=chess_stype;
    			logic_coor_chessboard[startrow][startcol]=chess_nochess;
    			if(chess_etype>=5&&chess_etype<=7)
    			{
    				logic_coor_chessboard[5][chess_etype-5]=chess_etype-5;
    				bonuschess_num[chess_etype-2]++;
    			}
    			if(chess_etype==8)
    			{
    				logic_coor_chessboard[5][0]=chess_chick;
    				bonuschess_num[3]++;
    			}
        	}
        	else 
        		return false;
        	break;
        case chess_rlion:
        	if(Math.abs(startrow-endrow)<=1&&Math.abs(startcol-endcol)<=1)
        	{
        		logic_coor_chessboard[endrow][endcol]=chess_stype;
    			logic_coor_chessboard[startrow][startcol]=chess_nochess;
    			if(chess_etype>=0&&chess_etype<=2)
    			{
    				logic_coor_chessboard[0][chess_etype]=chess_etype+5;
    				bonuschess_num[chess_etype]++;
    			}
    			if(chess_etype==3)
    			{
    				logic_coor_chessboard[0][0]=chess_rchick;
    				bonuschess_num[0]++;
    			}
        	}
        	else 
        		return false;
        	break;
        default: return false;  
      }
      return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(enable==false)
			return false;
		int x=(int)event.getX();		
		int y=(int)event.getY();
		//click must be on the chessboard
		if(x<32)
			return true;
		if(x>=288)
			return true;
		if(y<48)
			return true;
		if(y>=432)
			return true;
		//
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
				if(index_clicktime==0)
				{ 
					int tempcol=XconvertViewToLogic(x);
					int temprow=YconvertViewToLogic(y);	  
					if(temprow==0) //player cannot select computer`s bonus chess
						return true;
					if(logic_coor_chessboard[temprow][tempcol]==chess_nochess)//select a nochess board 
						return true;
					if(logic_coor_chessboard[temprow][tempcol]/5==1)//select a computer`s chess
						return true;
					selected_chessrow=temprow;
					selected_chesscol=tempcol;
					index_clicktime=1;
					break;
				}
				if(index_clicktime==1)
				{
					int tempcol=XconvertViewToLogic(x);
					int temprow=YconvertViewToLogic(y);
					if(temprow==0||temprow==5) //player cannot move a chess into a bonus row
					{
						selected_chesscol=-1;
						selected_chessrow=-1;
						index_clicktime=0;
						break;
					}
					if(logic_coor_chessboard[temprow][tempcol]/5==0) //player select a new chess on second click
					{
						selected_chesscol=tempcol;
						selected_chessrow=temprow;
						break;
					}
					else  //normal move
					{
						if(movechess(selected_chessrow,selected_chesscol,temprow,tempcol)==true)
						{
							saveChess();
						    computerMoveChess();
						    saveChess();
						}
						selected_chessrow=-1;
						selected_chesscol=-1;
						index_clicktime=0;
						break;
					}		
				}	
			}
		int i=CheckWin();
		if(i!=0)
		{
			Win(i);
		}
		invalidate();
		return true;
	}


	
	public void undo()
	{	
		if(gamestack.size()!=0)
		{
			gamestack.pop();
			bonus.pop();
			gamestack.pop();
			bonus.pop();
			recoverChess();
		}
		invalidate();
	}
}


