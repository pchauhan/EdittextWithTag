package com.sn.edittextwithtag;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean isTwise = false ;
    boolean isEdit = true ;
    EditText edtTag ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtTag = (EditText) findViewById(R.id.editText);
        double scaletype =getResources().getDisplayMetrics().density;
        if(scaletype >=3.0){
            isTwise = true ;
        }
        edtTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (count >= 1 && !isEdit) {
                    if (!Character.isSpaceChar(s.charAt(0))) {
                        if (s.charAt(start) == ' ')
                            setTag(); // generate chips
                    } else {
                        edtTag.getText().clear();
                        edtTag.setSelection(0);
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEdit) {
                    setTag();
                }
            }
        });

    }
    public void setTag() {
        if (edtTag.getText().toString().contains(" ")) // check comman in string
        {

            SpannableStringBuilder ssb = new SpannableStringBuilder(edtTag.getText());
            // split string wich comma
            String chips[] =edtTag.getText().toString().trim().split(" ");
            int x = 0;
            for (String c : chips) {
                LayoutInflater lf = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                TextView textView = (TextView) lf.inflate(
                        R.layout.tag_edittext, null);
                textView.setText(c); // set text
                int spec = MeasureSpec.makeMeasureSpec(0,
                        MeasureSpec.UNSPECIFIED);
                textView.measure(spec, spec);
                textView.layout(0, 0, textView.getMeasuredWidth(),
                        textView.getMeasuredHeight());
                Bitmap b = Bitmap.createBitmap(textView.getWidth(),
                        textView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(b);
                canvas.translate(-textView.getScrollX(), -textView.getScrollY());
                textView.draw(canvas);
                textView.setDrawingCacheEnabled(true);
                Bitmap cacheBmp = textView.getDrawingCache();
                Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
                textView.destroyDrawingCache(); // destory drawable
                BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
                int width = bmpDrawable.getIntrinsicWidth() ;
                int height = bmpDrawable.getIntrinsicHeight() ;
                if(isTwise){
                    width = width *2 ;
                    height = height *2;
                }
                bmpDrawable.setBounds(0, 0,width ,height);
                ssb.setSpan(new ImageSpan(bmpDrawable), x, x + c.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                x = x + c.length() + 1;
            }
            // set chips span
            isEdit = false ;
            edtTag.setText(ssb);
            // move cursor to last
            edtTag.setSelection(edtTag.getText().length());
        }

    }
    public  int convertDpToPixel(float dp){
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int)px;
    }


}
