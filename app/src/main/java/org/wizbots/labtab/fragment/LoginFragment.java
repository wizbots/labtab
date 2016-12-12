package org.wizbots.labtab.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.EditTextCustom;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.MentorsTable;
import org.wizbots.labtab.interfaces.CreateTokenListener;
import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.requesters.LoginRequester;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class LoginFragment extends ParentFragment implements View.OnClickListener, CreateTokenListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private HomeActivity homeActivityContext;
    private ProgressDialog progressDialog;
    private EditTextCustom editTextCustomEmail;
    private EditTextCustom editTextCustomPassword;
    private TextViewCustom textViewCustomForgotPassword;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        homeActivityContext = (HomeActivity) context;
        initView();
        initListeners();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(getActivity().getResources().getString(R.string.please_enter_your_name_and_password));
        labTabHeaderLayout.getMenuImageView().setVisibility(View.GONE);
        editTextCustomEmail = (EditTextCustom) rootView.findViewById(R.id.edt_email);
        editTextCustomPassword = (EditTextCustom) rootView.findViewById(R.id.edt_password);
        textViewCustomForgotPassword = (TextViewCustom) rootView.findViewById(R.id.tv_forgot_password);
        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(CreateTokenListener.class, this);
        rootView.findViewById(R.id.btn_login).setOnClickListener(this);
        rootView.findViewById(R.id.tv_forgot_password).setOnClickListener(this);
        editTextCustomEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    rootView.findViewById(R.id.btn_login).callOnClick();
                    handled = true;
                }
                return handled;
            }
        });
        editTextCustomPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    rootView.findViewById(R.id.btn_login).callOnClick();
                    handled = true;
                }
                return handled;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String email = editTextCustomEmail.getText().toString();
                String password = editTextCustomPassword.getText().toString();
                if (email.trim().length() == 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, LabTabConstants.PLEASE_ENTER_EMAIL);
                    editTextCustomEmail.requestFocus();
                    break;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, LabTabConstants.PLEASE_ENTER_VALID_EMAIL_ADDRESS);
                    editTextCustomEmail.requestFocus();
                    break;
                } else if (password.trim().length() == 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, LabTabConstants.PLEASE_ENTER_PASSWORD);
                    editTextCustomPassword.requestFocus();
                    break;
                }
                progressDialog.show();
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
                LabTabPreferences.getInstance(LabTabApplication.getInstance()).setEmailId(email);
                BackgroundExecutor.getInstance().execute(new LoginRequester(editTextCustomEmail.getText().toString(), editTextCustomPassword.getText().toString()));
                break;
            case R.id.tv_forgot_password:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(LabTabConstants.GO_TO_WIZBOTS_COM));
                if (browserIntent.resolveActivity(homeActivityContext.getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
                break;
        }
    }

    @Override
    public void tokenCreatedSuccessfully(CreateTokenResponse createTokenResponse) {
        progressDialog.dismiss();
        homeActivityContext.getSupportFragmentManager().popBackStack();
        LabTabPreferences.getInstance(LabTabApplication.getInstance()).setCreateTokenResponse(createTokenResponse);
        LabTabPreferences.getInstance(LabTabApplication.getInstance()).setUserLoggedIn(true);
        ArrayList<Mentor> mentors = new ArrayList<>();
        mentors.add(new Mentor(createTokenResponse.getId(), createTokenResponse.getMember_id(), createTokenResponse.getToken(), createTokenResponse.getDate()));
        MentorsTable.getInstance().insert(mentors);
        homeActivityContext.replaceFragment(FRAGMENT_HOME);
        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, LabTabConstants.LOGIN_SUCCESSFULL);
    }

    @Override
    public void unableToCreateToken(int responseCode) {
        progressDialog.dismiss();
        LabTabPreferences.getInstance(LabTabApplication.getInstance()).setUserLoggedIn(false);
        if (responseCode == SC_FORBIDDEN) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, LabTabConstants.PROVIDED_PASSWORD_OR_USERNAME_ARE_INVALID);
        } else if (responseCode == SC_NOT_FOUND) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, LabTabConstants.NOT_USER_WITH_PROVIDED_CREDENTIALS);
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, LabTabConstants.NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void onDestroy() {
        LabTabApplication.getInstance().removeUIListener(CreateTokenListener.class, this);
        super.onDestroy();
    }

    @Override
    public String getFragmentName() {
        return LoginFragment.class.getSimpleName();
    }


}
