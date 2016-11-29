package org.wizbots.labtab.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;

public class LabTabHeaderLayout extends FrameLayout {
    ImageView menuImageView;
    ImageView labTabTitleImageView;
    TextViewCustom dynamicTextViewCustom;

    public LabTabHeaderLayout(Context context) {
        super(context);
        initViewWithId(context);
    }

    public LabTabHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initView(attrs, context);
        } else {
            initView(attrs, context);
        }
    }


    public void initViewWithId(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_layout_for_lab_tab, this);
        menuImageView = (ImageView) findViewById(R.id.iv_menu);
        labTabTitleImageView = (ImageView) findViewById(R.id.iv_lab_tab_title);
        dynamicTextViewCustom = (TextViewCustom) findViewById(R.id.tv_dynamic);
    }

    public void initView(AttributeSet attrs, Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs,
                R.styleable.LabTabHeaderLayout, 0, 0);
        initViewWithId(context);
        boolean imageViewMenuVisibility = obtainStyledAttributes.getBoolean(R.styleable.LabTabHeaderLayout_menuIconVisibility, false);
        Drawable imageMenuDrawable = obtainStyledAttributes.getDrawable(R.styleable.LabTabHeaderLayout_menuIconSrc);
        Drawable labTabTitleDrawable = obtainStyledAttributes.getDrawable(R.styleable.LabTabHeaderLayout_labTitleSrc);
        String dynamicText = obtainStyledAttributes.getString(R.styleable.LabTabHeaderLayout_dynamicText);
        obtainStyledAttributes.recycle();
        setVisibilityForMenuImageView(imageViewMenuVisibility);
        setImageDrawableForImageView(menuImageView, imageMenuDrawable);
        setImageDrawableForImageView(labTabTitleImageView, labTabTitleDrawable);
        setDynamicText(dynamicText);
    }

    public void setVisibilityForMenuImageView(boolean visibilityForView) {
        if (!visibilityForView) {
            menuImageView.setVisibility(GONE);
        } else {
            menuImageView.setVisibility(VISIBLE);
        }
    }

    public void setImageDrawableForImageView(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public void setDynamicText(String text) {
        if (text != null) {
            dynamicTextViewCustom.setText(text);
        } else {
            dynamicTextViewCustom.setText(LabTabApplication.getInstance().getResources().getString(R.string.empty_string));
        }
    }

    public ImageView getMenuImageView() {
        return menuImageView;
    }

    public ImageView getLabTabTitleImageView() {
        return labTabTitleImageView;
    }

    public TextViewCustom getDynamicTextViewCustom() {
        return dynamicTextViewCustom;
    }
}
