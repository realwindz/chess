package com.my.chess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;



public class Chess extends Activity{
	private ChessView myChessView;
	//private String Tag="chess";
	//private TextView myTextView;
	final int MENU_NEW=Menu.FIRST;
	final int MENU_UNDO=Menu.FIRST+1;

	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.main);
        myChessView=(ChessView)findViewById(R.id.myChessView);
	    myChessView.setTv((TextView)findViewById(R.id.myTextView));
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,MENU_NEW, 0, "New Game");
		menu.add(0,MENU_UNDO, 0, "Undo");
		return true;
		//return super.onCreateOptionsMenu(menu);
	}

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
	   super.onOptionsItemSelected(item);
	   switch(item.getItemId()){
	   case MENU_NEW:
		  
		   myChessView.initGame();
		   myChessView.invalidate();
		   myChessView.enable=true;
		   break;
	   case MENU_UNDO:
		   myChessView.undo();
		   break;
	   }
	   return false;
	}
        
}