package apps.lnsel.com.vhortexttest.helpers.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.rockerhieu.emojicon.EmojiconTextView;

import apps.lnsel.com.vhortexttest.R;


public class ChatTextView extends EmojiconTextView {

	public ChatTextView(Context context) {
		super(context);
	}

	public ChatTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ViewStyle);
		String customFont = a.getString(R.styleable.ViewStyle_customFont);
//		setCustomFont(ctx, customFont);
		a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), asset);
		} catch (Exception e) {
			return false;
		}
		setTypeface(tf);
		return true;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
	}
}