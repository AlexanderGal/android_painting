package com.sbthomework.painter;

import android.graphics.PorterDuff;
import android.graphics.drawable.VectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView clearButton;
    private TextView rectButton;
    private TextView cirleButton;
    private TextView lineButton;
    private TextView eraserButton;
    private TextView colorButton;
    private ImageView currentImage;
    private Spinner drawableButton;
    private PainterView painterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutElementInit();
        layoutElementsOnClickListners();
    }

    private void layoutElementsOnClickListners() {

        clearButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                painterView.clear();
                painterView.sClear();
            }
        });

        rectButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                painterView.setCurrentCanvasType(PainterView.RECT_DRAW);
            }
        });

        cirleButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                painterView.setCurrentCanvasType(PainterView.CIRCLE_DRAW);
            }
        });

        lineButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                painterView.setCurrentCanvasType(PainterView.LINE_DRAW);
            }
        });

        eraserButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                painterView.setCurrentCanvasType(PainterView.ERASER);
            }
        });


        drawableButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id==0){
                    painterView.setDrawable(R.drawable.rect_drawable);
                }else {
                    painterView.setDrawable(R.drawable.circle_drawable);
                }
                painterView.setCurrentCanvasType(PainterView.DRAWABLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                painterView.setCurrentDrawableType(parent.getFirstVisiblePosition());
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                HSVColorPickerDialog cpd = new HSVColorPickerDialog( MainActivity.this, 0xFF4488CC, new HSVColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void colorSelected(Integer color) {
                        currentImage.setBackgroundColor(color);
                        painterView.setColor(color);
                    }
                });
                cpd.setTitle( getString(R.string.color_select_title) );
                cpd.show();
            }
        });
    }

    private void layoutElementInit() {
        clearButton = (TextView) findViewById(R.id.clear_button);
        rectButton = (TextView) findViewById(R.id.choose_rect_button);
        cirleButton = (TextView) findViewById(R.id.choose_circle_button);
        lineButton = (TextView) findViewById(R.id.choose_line_button);
        drawableButton = (Spinner) findViewById(R.id.choose_drawable);
        colorButton = (TextView) findViewById(R.id.choose_color);
        eraserButton = (TextView) findViewById(R.id.choose_eraser);
        currentImage = (ImageView) findViewById(R.id.current_color);
        painterView = (PainterView) findViewById(R.id.paint_view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drawable_select, R.layout.drawable_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drawableButton.setAdapter(adapter);

    }

}
