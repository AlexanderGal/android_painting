package com.sbthomework.painter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView clearButton;
    private TextView rectButton;
    private TextView cirleButton;
    private TextView lineButton;
    private TextView eraserButton;
    private TextView colorButton;
//    private TextView textButton;
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

//        textButton.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick(View v) {
//                painterView.setCurrentCanvasType(PainterView.LINE_DRAW);
//            }
//        });

        colorButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                painterView.setCurrentCanvasType(PainterView.LINE_DRAW);
            }
        });
    }

    private void layoutElementInit() {
        clearButton = (TextView) findViewById(R.id.clear_button);
        rectButton = (TextView) findViewById(R.id.choose_rect_button);
        cirleButton = (TextView) findViewById(R.id.choose_circle_button);
        lineButton = (TextView) findViewById(R.id.choose_line_button);
//        textButton = (TextView) findViewById(R.id.choose_text_button);
        colorButton = (TextView) findViewById(R.id.choose_color);
        eraserButton = (TextView) findViewById(R.id.choose_eraser);
        painterView = (PainterView) findViewById(R.id.paint_view);

    }
}
