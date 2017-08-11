package kr.ac.skuniv.myapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.ac.skuniv.myapp.R;

/**
 * StatusBar LinearLayout with Custom State Attributes 
 * @author kickscar
 *
 */
public class StatusBarLinearLayout extends LinearLayout {

	private static final int[] STATE_STATUS_INFORMATION = { R.attr.state_status_information };
	private static final int[] STATE_STATUS_WARNING = { R.attr.state_status_warning };
	private static final int[] STATE_STATUS_ERROR = { R.attr.state_status_error };
	
	private int status = STATUS.NORMAL;
	private boolean visible = false;
	
	public StatusBarLinearLayout( Context context ) {
		super(context);
	}
	
	public StatusBarLinearLayout( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}
	
	@Override
	protected int[] onCreateDrawableState( int extraSpace ) {
		final int[] drawableState = super.onCreateDrawableState( extraSpace + 1 );

		if( status == STATUS.INFORMATION ) {
			mergeDrawableStates( drawableState, STATE_STATUS_INFORMATION );
		} else if( status == STATUS.ERROR ) {
			mergeDrawableStates( drawableState, STATE_STATUS_ERROR );
		} else if( status == STATUS.WARNING ) {
			mergeDrawableStates( drawableState, STATE_STATUS_WARNING );
		} 
		
		return drawableState;
	}
	
	public void setStatus( int status ) {
		if( this.status != status ) {
			this.status = status;
			invalidate();			
			refreshDrawableState();
		}
	}
	
	public StatusBarLinearLayout makeText( int status, String text ) {
		setStatus( status );
		
		int count = getChildCount();
		for( int i = 0; i < count; i++ ) {
			View view = getChildAt( i );
			if( view instanceof TextView ) {
				( ( TextView ) view ).setText( text );
				break;
			}
		}
		
		return this;
	}
	
	public void show( boolean show ) {
		if( show == false ) {
			TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1 * getMeasuredHeight() );
    		animation.setDuration( 1000 );
    		startAnimation(animation);
    		setVisibility( View.GONE );
		} else {
        	setVisibility( View.VISIBLE );
        	TranslateAnimation animation = new TranslateAnimation(0, 0, -1 * getMeasuredHeight(), 0 );
        	animation.setDuration( 1000 );
        	startAnimation(animation);
		}
		visible = show;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public static final class STATUS {
		public static final int NORMAL  = 0;
		public static final int INFORMATION  = 1;
		public static final int WARNING  = 2;
		public static final int ERROR  = 3;
	}
}
