package com.example.gonggong;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    @Override
    public void setChecked(boolean b) {
        CheckBox checkBox=findViewById(R.id.deleteCheckBox);

        if(checkBox.isChecked()!=b)
            checkBox.setChecked(b);
    }

    @Override
    public boolean isChecked() {
        CheckBox checkBox=findViewById(R.id.deleteCheckBox);
        return checkBox.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox checkBox=findViewById(R.id.deleteCheckBox);
        setChecked(!checkBox.isChecked());
    }


}
