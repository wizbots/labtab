package org.wizbots.labtab.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.wizbots.labtab.R;
import org.wizbots.labtab.interfaces.VideoOptionSelector;

public class DialogueUtil {


    private DialogueUtil() {
    }

    /**
     * static method for showing video option dialogue choose from gallery or record video
     *
     * @param context
     * @param videoOptionSelector is an call back
     */
    public static void showVideoOptionDialogue(Context context, final VideoOptionSelector videoOptionSelector) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View rootView;
        if (inflater != null) {
            rootView = inflater.inflate(R.layout.dialogue_choose_video, null);
            View vChooseExisting = rootView.findViewById(R.id.v_choose_existing);
            View vRecord = rootView.findViewById(R.id.v_record_from_camera);
            ImageView ivClose = (ImageView) rootView.findViewById(R.id.iv_close);
            dialogBuilder.setView(rootView);
            dialogBuilder.setCancelable(false);
            final Dialog dialog = dialogBuilder.create();
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    if (videoOptionSelector != null) {
                        videoOptionSelector.onCancelled();
                    }


                }
            });
            vChooseExisting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (videoOptionSelector != null) {
                        videoOptionSelector.onGalleryClick();
                    }


                }
            });
            vRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (videoOptionSelector != null) {
                        videoOptionSelector.onCameraClick();
                    }


                }
            });
            dialog.show();
        }


    }


    public static void showConfirmDialog(Context context, int titleResourceId, int messageResourceId, DialogInterface.OnClickListener positiveListener) {
        showConfirmDialog(context, context.getString(titleResourceId), context.getString(messageResourceId), null, positiveListener);
    }

    public static void showConfirmDialog(Context context, int titleResourceId, int messageResourceId, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener positiveListener) {
        showConfirmDialog(context, context.getString(titleResourceId), context.getString(messageResourceId), negativeListener, positiveListener);
    }

    public static void showConfirmDialog(Context context, String title, String message, final DialogInterface.OnClickListener negativeListener, final DialogInterface.OnClickListener positiveListener) {

        showConfirmDialog(context, title, message, context.getString(R.string.no), negativeListener, context.getString(R.string.yes), positiveListener);
    }

    /**
     *
     * @param context
     * @param title title of dialog
     * @param message message for dialog
     * @param negativeButtonName negative button name
     * @param negativeListener negative button callback for ex :No
     * @param positiveButtonName positive button call back for ex: Yes
     * @param positiveListener positive button callback
     */
    public static void showConfirmDialog(Context context, String title, String message, String negativeButtonName, final DialogInterface.OnClickListener negativeListener, String positiveButtonName, final DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(negativeButtonName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (negativeListener != null) negativeListener.onClick(dialog, which);
                    }
                })
                .setPositiveButton(positiveButtonName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (positiveListener != null) positiveListener.onClick(dialog, which);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .show();
    }


}
