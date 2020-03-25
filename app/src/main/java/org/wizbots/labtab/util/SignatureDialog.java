package org.wizbots.labtab.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.ButtonCustom;

public class SignatureDialog extends Dialog {

    private CaptureSignatureView mSig;

    public SignatureDialog(Context context, ClickListener save){
        super(context);
        this.mListener = save;
    }

    private ClickListener mListener;

    public interface ClickListener {
        void onConfirmClick();
        void onCancelClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_signature);

        LinearLayout signatureLayout = (LinearLayout) findViewById(R.id.signature_layout);
        ButtonCustom cancelButton = (ButtonCustom) findViewById(R.id.signature_cancel_button);
        ButtonCustom confirmButton = (ButtonCustom) findViewById(R.id.signature_confirm_button);

        mSig = new CaptureSignatureView(getContext(), null);
        signatureLayout.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClick();
            }
        });

       confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirmClick();
            }
        });
    }

    public Bitmap getSigBitmap(){
        return mSig.getBitmap();
    }

    public void clearSig(){
        mSig.ClearCanvas();
    }

    public Boolean isSigAvailable(){
        return mSig.isThereAnySignature();
    }
}
