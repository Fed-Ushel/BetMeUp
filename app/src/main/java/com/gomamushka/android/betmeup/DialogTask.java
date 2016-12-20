package com.gomamushka.android.betmeup;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.res.Resources;

/**
 * Created by F_Aredakov on 19.12.2016.
 */
public class DialogTask extends DialogFragment implements View.OnClickListener {

        private Integer cancelBet;
        private String pTask;
        private Resources res;
    // игра
    private Game game;

    /* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    // Ттак как onAttach(Activity activity) не поддерживается в новых версиях должно два метода
    // Описано тут - http://stackoverflow.com/questions/32083053/android-fragment-onattach-deprecated
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
          Activity  a = (Activity) context;

            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
                mListener = (NoticeDialogListener) a;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(a.toString()
                        + " must implement NoticeDialogListener");
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            super.onAttach(activity);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
                mListener = (NoticeDialogListener) activity;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(activity.toString()
                        + " must implement NoticeDialogListener");
            }
        }
    }


   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = Game.getInstance();

        //передаем в диалоговое окно сумму счета и текст задания (из экземпляра класса Game)

        cancelBet = game.activePlayer.account.amount / 10;
        getDialog().setTitle(R.string.task);

        View v = inflater.inflate(R.layout.task_dialog, null);
        TextView tv = (TextView) v.findViewById(R.id.textTask_dialog_layout);
       tv.setText(game.getTurnTask());


        Button cancelButton = (Button) v.findViewById(R.id.btnNo_dialog_layout);
        //Добавляем сумму штрафа за отмену на кнопку
         res = getResources();
         String text = res.getString(R.string.button_decline_task, cancelBet );
        cancelButton.setText(text);
        cancelButton.setOnClickListener(this);
        v.findViewById(R.id.btnYes_dialog_layout).setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnNo_dialog_layout:
                game.activePlayer.account.reduceAmount(cancelBet);
                mListener.onDialogNegativeClick(DialogTask.this);
               dismiss();
                break;

            case R.id.btnYes_dialog_layout:
                mListener.onDialogPositiveClick(DialogTask.this);
                dismiss();
                break;
        }

    }


}
