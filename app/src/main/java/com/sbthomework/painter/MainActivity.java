package com.sbthomework.painter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView clearButton;
    private TextView rectButton;
    private TextView cirleButton;
    private TextView lineButton;
    private Switch modeSwitch;
    private PainterView painterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutElementInit();
        layoutElementsOnClickListners();
    }

    private void layoutElementsOnClickListners() {
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    painterView.setMode(PainterView.ERASE_MODE);
                } else {
                    painterView.setMode(PainterView.MULTY_MODE);
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                painterView.clear();
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
    }

    private void layoutElementInit() {
        modeSwitch = (Switch) findViewById(R.id.switchMode);
        clearButton = (TextView) findViewById(R.id.clear_button);
        rectButton = (TextView) findViewById(R.id.choose_rect_button);
        cirleButton = (TextView) findViewById(R.id.choose_circle_button);
        lineButton = (TextView) findViewById(R.id.choose_line_button);
        painterView = (PainterView) findViewById(R.id.paint_view);
    }
}
