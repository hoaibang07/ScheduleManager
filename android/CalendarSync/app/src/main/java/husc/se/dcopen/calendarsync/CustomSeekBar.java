package husc.se.dcopen.calendarsync;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class CustomSeekBar extends SeekBar {
    private static final int NUMBER_ZERO = 30;
    private static final int NUMBER_MAX = 60;
    private Rect rect;
    private Paint paint ;
    private int seekbar_height;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        rect = new Rect();
        paint = new Paint();
        seekbar_height = 6;
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        rect.set(0 + getThumbOffset(),
                (getHeight() / 2) - (seekbar_height/2),
                getWidth()- getThumbOffset(),
                (getHeight() / 2) + (seekbar_height/2));

        paint.setColor(Color.GRAY);
        canvas.drawRect(rect, paint);
        if (this.getProgress() > NUMBER_ZERO) {
            if(this.getProgress() != NUMBER_MAX) {
                rect.set(getWidth() / 2,
                        (getHeight() / 2) - (seekbar_height/2),
                        getWidth() / 2 + (getWidth() / NUMBER_MAX) * (getProgress() - NUMBER_ZERO),
                        getHeight() / 2 + (seekbar_height/2));

            } else {
                rect.set(getWidth() / 2,
                        (getHeight() / 2) - (seekbar_height/2),
                        getWidth() / 2 + (getWidth() / NUMBER_MAX) * (getProgress() - NUMBER_ZERO),
                        getHeight() / 2 + (seekbar_height/2));
            }

            paint.setColor(Color.GREEN);
            canvas.drawRect(rect, paint);
        }

        if (this.getProgress() < NUMBER_ZERO) {
            rect.set(getWidth() / 2 - ((getWidth() / NUMBER_MAX) * (NUMBER_ZERO - getProgress())),
                    (getHeight() / 2) - (seekbar_height/2),
                    getWidth() / 2,
                    getHeight() / 2 + (seekbar_height/2));

            paint.setColor(Color.GREEN);
            canvas.drawRect(rect, paint);
        }

        super.onDraw(canvas);
    }
}
