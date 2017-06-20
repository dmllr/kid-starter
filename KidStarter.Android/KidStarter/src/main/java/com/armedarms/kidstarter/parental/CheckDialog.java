package com.armedarms.kidstarter.parental;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.armedarms.kidstarter.R;
import com.armedarms.kidstarter.utils.MediaUtils;

import java.util.Random;

public class CheckDialog extends Dialog {

    private static CheckDialog instance;

    private final Context context;
    private final View view;

    private ResultListener listener;

    private final TextView textExpression;
    private final EditText textAnswer;

    int a, b, c, realAnswer, gotAnswer;

    public static CheckDialog getInstance() {
        return instance;
    }


    public CheckDialog(Context context) {
        super(context);

        instance = this;

        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_check, null);

        Random random = new Random();
        a = random.nextInt(12);
        b = random.nextInt(12);
        c = 1 + random.nextInt(5);
        realAnswer = (a + b) * c;

        textExpression = (TextView)view.findViewById(R.id.expression);
        textAnswer = (EditText)view.findViewById(R.id.answer);

        textExpression.setText(String.format("(%d + %d) * %d", a, b, c));

        view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotAnswer = Integer.parseInt("0" + textAnswer.getText().toString());
                if (gotAnswer == realAnswer) {
                    listener.onCheckPassed();
                    dismiss();
                } else {
                    MediaUtils.animateNegative(view);
                }
            }
        });
        view.findViewById(android.R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(view);
    }

    public CheckDialog withListener(ResultListener listener) {
        this.listener = listener;
        return this;
    }

    public interface ResultListener {
        public abstract void onCheckPassed();
    }
}
