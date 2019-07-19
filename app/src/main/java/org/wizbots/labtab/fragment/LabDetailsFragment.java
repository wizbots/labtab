package org.wizbots.labtab.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.LabDetailsAdapter;
import org.wizbots.labtab.adapter.SelectedStudentAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.ProgramTable;
import org.wizbots.labtab.interfaces.LabDetailsAdapterClickListener;
import org.wizbots.labtab.interfaces.OnSyncDoneListener;
import org.wizbots.labtab.interfaces.requesters.AddWizchipsListener;
import org.wizbots.labtab.interfaces.requesters.GetProgramStudentsListener;
import org.wizbots.labtab.interfaces.requesters.MarkStudentAbsentListener;
import org.wizbots.labtab.interfaces.requesters.PromotionDemotionListener;
import org.wizbots.labtab.interfaces.requesters.WithdrawWizchipsListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.program.response.ProgramResponse;
import org.wizbots.labtab.requesters.AddWizchipsRequester;
import org.wizbots.labtab.requesters.MarkStudentAbsentRequester;
import org.wizbots.labtab.requesters.ProgramStudentsRequester;
import org.wizbots.labtab.requesters.PromotionDemotionRequester;
import org.wizbots.labtab.requesters.WithdrawWizchipsRequester;
import org.wizbots.labtab.service.SyncManager;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LabDetailsFragment extends ParentFragment implements LabDetailsAdapterClickListener,
        GetProgramStudentsListener, View.OnClickListener, MarkStudentAbsentListener, PromotionDemotionListener,
        WithdrawWizchipsListener, AddWizchipsListener, OnSyncDoneListener {
    public static final String PROGRAM = "PROGRAM";
    public static final String STUDENT = "STUDENT";
    public static final String SELECTED_STUDENTS = "SELECTED_STUDENTS";
    public static final String LAB_LEVEL = "LAB_LEVEL";
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private LabDetailsAdapter labDetailsAdapter;
    private RecyclerView recyclerViewLabDetails;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
//    private ImageView calendarImageView;
    private DatePickerDialog datePickerDialog;
    private ProgramOrLab programOrLab;
    private Program program;
    private ProgressDialog progressDialog;
    private TextViewCustom labSKUTextViewCustom, availabilityTextViewCustom,
            nameTextViewCustom, locationTextViewCustom, categoryTextViewCustom,
            roomTextViewCustom, gradesTextViewCustom, priceTextViewCustom,
            fromTextViewCustom, toTextViewCustom, timeSlotTextViewCustom, dayTextViewCustom;
    private TextView markAbsentTextView, promoteTextView, demoteTextView;
//    private CheckBox checkBoxSendNotification;
    private Date dateSelected;
    boolean status[];

    public LabDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LabTabApplication.getInstance().addUIListener(OnSyncDoneListener.class, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lab_details, container, false);
        homeActivityContext = (HomeActivity) context;
        programOrLab = getArguments().getParcelable(LabListFragment.LAB);
        initView();
        initListeners();
        BackgroundExecutor.getInstance().execute(new ProgramStudentsRequester(programOrLab.getId()));
        return rootView;
    }

    @Override
    public void onResume() {
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.LAB_DETAILS);
        super.onResume();
        SyncManager.getInstance().onRefreshData(1);
        boolean isSync = SyncManager.getInstance().isLabDetailSynced();
        if (isSync) {
            updateSyncStatus(true);
        } else {
            updateSyncStatus(false);
        }
    }

    @Override
    public String getFragmentName() {
        return LabDetailsFragment.class.getSimpleName();
    }

    public void initView() {
        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        markAbsentTextView = (TextView) rootView.findViewById(R.id.tv_mark_absent);
        demoteTextView = (TextView) rootView.findViewById(R.id.tv_promote);
        promoteTextView = (TextView) rootView.findViewById(R.id.tv_demote);
//        checkBoxSendNotification = (CheckBox) rootView.findViewById(R.id.cb_send_absent_notification);
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.LAB_DETAILS);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        initHeaderView();

        recyclerViewLabDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_lab_details);
//        recyclerViewLabDetails.setFocusable(false);
        objectArrayList = new ArrayList<>();

        labDetailsAdapter = new LabDetailsAdapter(objectArrayList, homeActivityContext, this);
        labDetailsAdapter.registerAdapterDataObserver(observer);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabDetails.setLayoutManager(mLayoutManager);
        recyclerViewLabDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabDetails.setAdapter(labDetailsAdapter);
        rootView.findViewById(R.id.btn_absences).setOnClickListener(this);
        rootView.findViewById(R.id.btn_additional).setOnClickListener(this);
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());

//        calendarImageView = (ImageView) rootView.findViewById(R.id.iv_calendar);
//
//        calendarImageView.setOnClickListener(this);
        boolean isNetwork = LabTabApplication.getInstance().isNetworkAvailable();
        program = ProgramTable.getInstance().getProgramByProgramId(programOrLab.getId());
        if (!isNetwork && program == null) {
            progressDialog.dismiss();
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_NO_CONNECTION);
        } else if (program != null) {
            setHeaderView(program);
            ArrayList<Student> studentArrayList = ProgramStudentsTable.getInstance().getStudentsListByProgramId(programOrLab.getId());
            if (!isNetwork && (studentArrayList == null || studentArrayList.isEmpty())) {
                progressDialog.dismiss();
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_NO_CONNECTION);
            } else if (!studentArrayList.isEmpty()) {
                objectArrayList.addAll(studentArrayList);
                maintnedCheckedStudentsStatus();
                labDetailsAdapter.notifyDataSetChanged();
            } else {
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_STUDENT_FOUND_FOR_THIS_LAB);
            }
            progressDialog.dismiss();
        } else {
            BackgroundExecutor.getInstance().execute(new ProgramStudentsRequester(programOrLab.getId()));
        }

        rootView.findViewById(R.id.iv_add_video).setOnClickListener(this);
        markAbsentTextView.setOnClickListener(this);
        promoteTextView.setOnClickListener(this);
        demoteTextView.setOnClickListener(this);
        rootView.findViewById(R.id.tv_plus).setOnClickListener(this);
        rootView.findViewById(R.id.tv_minus).setOnClickListener(this);
    }

    protected RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            progressDialog.dismiss();
        }
    };

    @Override
    public void onActionViewClick(Student student) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROGRAM, program);
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putParcelable(STUDENT, student);
        bundle.putString(LAB_LEVEL, programOrLab.getLevel().toUpperCase());
        homeActivityContext.replaceFragment(Fragments.STUDENT_LAB_DETAILS, bundle);
    }

    @Override
    public void onActionEditClick(Student student) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROGRAM, program);
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putParcelable(STUDENT, student);
        bundle.putString(LAB_LEVEL, programOrLab.getLevel().toUpperCase());
        homeActivityContext.replaceFragment(Fragments.STUDENT_PROFILE, bundle);
    }

    @Override
    public void onActionCloseToNextLevelClick(Student student) {
        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "On Action Close To Next Level");
    }

    @Override
    public void onCheckChanged(int position, boolean value) {
        ((Student) objectArrayList.get(position)).setCheck(value);
        status[position] = value;


    }

    private void showCalendar() {
            }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetProgramStudentsListener.class, this);
        LabTabApplication.getInstance().addUIListener(MarkStudentAbsentListener.class, this);
        LabTabApplication.getInstance().addUIListener(PromotionDemotionListener.class, this);
        LabTabApplication.getInstance().addUIListener(WithdrawWizchipsListener.class, this);
        LabTabApplication.getInstance().addUIListener(AddWizchipsListener.class, this);
    }

    @Override
    public void programStudentsFetchedSuccessfully(ProgramResponse programResponse, final Program program, final ArrayList<Student> studentArrayList, ArrayList<Absence> absenceArrayList) {
        this.program = program;

        if (studentArrayList.isEmpty()) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_STUDENT_FOUND_FOR_THIS_LAB);
            return;
        }

        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setHeaderView(program);
                notifyLabDetailsAdapter();
            }
        });
    }

    @Override
    public void unableToFetchProgramStudents(int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
        if (responseCode == StatusCode.FORBIDDEN) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_LAB_DETAIL_FOR_THIS_LAB);
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void onDestroy() {
        progressDialog.dismiss();
        LabTabApplication.getInstance().removeUIListener(OnSyncDoneListener.class, this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LabTabApplication.getInstance().removeUIListener(GetProgramStudentsListener.class, this);
        LabTabApplication.getInstance().removeUIListener(MarkStudentAbsentListener.class, this);
        LabTabApplication.getInstance().removeUIListener(PromotionDemotionListener.class, this);
        LabTabApplication.getInstance().removeUIListener(AddWizchipsListener.class, this);
        LabTabApplication.getInstance().removeUIListener(WithdrawWizchipsListener.class, this);
    }

    public void initHeaderView() {
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);
        availabilityTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_availability);
        nameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_name);
        locationTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_location);
        categoryTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_category);
        roomTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_room);
        gradesTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_grades);
        priceTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_price);
        fromTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_from);
        toTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_to);
        timeSlotTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_time_slot);
        dayTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_day);

        labSKUTextViewCustom.setText("----");
        availabilityTextViewCustom.setText("----");
        nameTextViewCustom.setText("----");
        locationTextViewCustom.setText("----");
        categoryTextViewCustom.setText("----");
        roomTextViewCustom.setText("----");
        gradesTextViewCustom.setText("----");
        priceTextViewCustom.setText("----");
        fromTextViewCustom.setText("----");
        toTextViewCustom.setText("----");
        timeSlotTextViewCustom.setText("----");
        dayTextViewCustom.setText("----");
    }

    public void setHeaderView(Program program) {
        labSKUTextViewCustom.setText(String.valueOf(program.getSku()));
        availabilityTextViewCustom.setText(program.getAvailability());
        nameTextViewCustom.setText(program.getName());
        locationTextViewCustom.setText(program.getLocation());
        categoryTextViewCustom.setText(program.getCategory());
        roomTextViewCustom.setText(program.getRoom());
        gradesTextViewCustom.setText(program.getGrades());
        priceTextViewCustom.setText(program.getPrice());
        fromTextViewCustom.setText(program.getStarts());
        toTextViewCustom.setText(program.getEnds());
        timeSlotTextViewCustom.setText(program.getTime_slot());
        dayTextViewCustom.setText(LabTabUtil.getFormattedDate(DateFormat.DEFAULT, new Date()));
    }

    private void showMarkAbsentDialog(final ArrayList<Student> studentArrayList) {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_mark_absent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = getResources().getDimensionPixelOffset(R.dimen.login_container_width);
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.setTitle(R.string.mark_absent);

        dateSelected = Calendar.getInstance().getTime();
        final TextViewCustom tvMarkAbsentOn = (TextViewCustom) dialog.findViewById(R.id.tv_mark_absent_on);

        tvMarkAbsentOn.setText(getString(R.string.mark_absence_for_the_selected_kids_on)+" "+new SimpleDateFormat("dd MMM, yyy").format(dateSelected));
        ImageView calender = (ImageView) dialog.findViewById(R.id.iv_calendar);
        RecyclerView selectedStudentList = (RecyclerView) dialog.findViewById(R.id.selected_student_list);
        selectedStudentList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        selectedStudentList.setAdapter(new SelectedStudentAdapter(studentArrayList,getActivity()));
        final CheckBox cbSendAbsentNotification = (CheckBox) dialog.findViewById(R.id.cb_send_absent_notification);
        Button confirm = (Button) dialog.findViewById(R.id.bt_confirm);
        Button cancel = (Button) dialog.findViewById(R.id.bt_cancel);

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (LabTabUtil.isValidDateSelection(calendar.getTime())) {
                            dateSelected = calendar.getTime();
                            tvMarkAbsentOn.setText(getString(R.string.mark_absence_for_the_selected_kids_on)+" "+new SimpleDateFormat("dd MMM, yyy").format(dateSelected));
                        } else {
                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.YOU_CAN_NOT_SELECT_DATE_MORE_THAN_TODAY);
                        }
                    }

                };
                new DatePickerDialog(homeActivityContext,
                        date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                BackgroundExecutor.getInstance().execute(new MarkStudentAbsentRequester(studentArrayList,
                        LabTabUtil.getFormattedDate(DateFormat.YYYYMMDD, dateSelected),
                        program,
                        cbSendAbsentNotification.isChecked()));
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }



    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.tv_mark_absent:
                if (!objectArrayList.isEmpty()) {

                    ArrayList<Student> studentArrayList = getSelectedStudents();
                    if (!studentArrayList.isEmpty()) {
                        showMarkAbsentDialog(studentArrayList);
                        unCheckAllStudents();
//                        if (dateSelected == null) {
//                            showMarkAbsentDialog(studentArrayList);
//                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PLEASE_SELECT_DATE_FIRST);
//                            return;
//                        }
//                        if (dateSelected != null) {
//                            progressDialog.show();
//                            BackgroundExecutor.getInstance().execute(new MarkStudentAbsentRequester(studentArrayList,
//                                    LabTabUtil.getFormattedDate(DateFormat.YYYYMMDD, dateSelected),
//                                    program,
//                                    checkBoxSendNotification.isChecked()));
//                        } else {
//                            progressDialog.show();
//                            BackgroundExecutor.getInstance().execute(new MarkStudentAbsentRequester(studentArrayList,
//                                    LabTabUtil.getFormattedDate(DateFormat.YYYYMMDD, new Date()),
//                                    program,
//                                    checkBoxSendNotification.isChecked()));
//                        }
                    } else {
                        progressDialog.dismiss();
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PLEASE_SELECT_AT_LEAST_ONE_STUDENT_TO_MARK_ABSENT);
                    }
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_MARK_ABSENT_FOR_THIS_LAB);
                }
                break;
            case R.id.tv_promote:
                if (!objectArrayList.isEmpty()) {
                    progressDialog.show();
                    ArrayList<Student> promoteStudents = getSelectedStudents();
                    if (!promoteStudents.isEmpty()) {
                        if (promoteStudents.size() == 1 && !promoteStudents.get(0).getPromotionDemotionSync().equals(SyncStatus.PROMOTION_DEMOTION_SYNCED)) {
                            progressDialog.dismiss();
                            notifyLabDetailsAdapter();
                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_ALREADY_PROMOTED_DEMOTED_BY_ONE_LEVEL);
                            unCheckAllStudents();
                        } else {
                            BackgroundExecutor.getInstance().execute(new PromotionDemotionRequester(promoteStudents,
                                    program,
                                    true));
                            unCheckAllStudents();
                        }
                    } else {
                        progressDialog.dismiss();
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PLEASE_SELECT_AT_LEAST_ONE_STUDENT_TO_PROMOTE);
                    }
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.AT_LEAST_ONE_STUDENT_IS_NEEDED_FOR_PROMOTION_FOR_THIS_LAB);
                }
                break;
            case R.id.tv_demote:
                if (!objectArrayList.isEmpty()) {
                    progressDialog.show();
                    ArrayList<Student> demoteStudents = getSelectedStudents();
                    if (!demoteStudents.isEmpty()) {
                        if (demoteStudents.size() == 1 && !demoteStudents.get(0).getPromotionDemotionSync().equals(SyncStatus.PROMOTION_DEMOTION_SYNCED)) {
                            progressDialog.dismiss();
                            notifyLabDetailsAdapter();
                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_ALREADY_PROMOTED_DEMOTED_BY_ONE_LEVEL);
                            unCheckAllStudents();
                        } else {
                            BackgroundExecutor.getInstance().execute(new PromotionDemotionRequester(demoteStudents,
                                    program,
                                    false));
                            unCheckAllStudents();
                        }
                    } else {
                        progressDialog.dismiss();
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PLEASE_SELECT_AT_LEAST_ONE_STUDENT_TO_DEMOTE);
                    }
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.AT_LEAST_ONE_STUDENT_IS_NEEDED_FOR_DEMOTION_FOR_THIS_LAB);
                }
                break;
            case R.id.iv_add_video:
                if (!objectArrayList.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                    bundle.putSerializable(SELECTED_STUDENTS, getSelectedStudents());
                    bundle.putString(LAB_LEVEL, programOrLab.getLevel().toUpperCase());
                    homeActivityContext.replaceFragment(Fragments.ADD_VIDEO, bundle);
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_ADD_VIDEO_FOR_THIS_LAB);
                }
                break;
            case R.id.btn_absences:
                Bundle listOfSkipBundle = new Bundle();
                listOfSkipBundle.putParcelable(PROGRAM, program);
                listOfSkipBundle.putString(LAB_LEVEL, programOrLab.getLevel().toUpperCase());
                homeActivityContext.replaceFragment(Fragments.LIST_OF_SKIPS, listOfSkipBundle);
                break;
            case R.id.btn_additional:
                Bundle additionalBundle = new Bundle();
                additionalBundle.putParcelable(PROGRAM, program);
                additionalBundle.putString(LAB_LEVEL, programOrLab.getLevel().toUpperCase());
                homeActivityContext.replaceFragment(Fragments.ADDITIONAL_INFORMATION, additionalBundle);
                break;
            case R.id.iv_calendar:
                showCalendar();
                break;
            case R.id.tv_plus:
                ArrayList<Student> studentList = getSelectedStudents();
                if (studentList.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_INCREMENT);
                } /*else if (studentList.size() > 1) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_INCREMENT);
                }*/ else {
                    Student student = studentList.get(0);
                    List<String> selectted = new ArrayList<>();
                    for (Student student1 : studentList) {
                        selectted.add(student1.getStudent_id());
                    }
                    progressDialog.show();
                    BackgroundExecutor.getInstance().execute(new AddWizchipsRequester(programOrLab.getId(), selectted, 1));
                    unCheckAllStudents();
                }
                break;
            case R.id.tv_minus:
                studentList = getSelectedStudents();
                List<String> selectted = new ArrayList<>();

                if (studentList.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_DECREMENT);
                } /*else if (studentList.size() > 1) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_DECREMENT);
                }*/ else {
                    Student student = studentList.get(0);
                    if (getChips(student.getWizchips(), student.getOfflinewizchips()) <= 0) {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.ALREADY_MINIMUM_WIZCHIPS);
                    } else {
                        progressDialog.show();
                    }
                    for (Student student1 : studentList) {
                        selectted.add(student1.getStudent_id());
                    }
                    progressDialog.show();
                 //   BackgroundExecutor.getInstance().execute(new AddWizchipsRequester(programOrLab.getId(), selectted, 1));
                    BackgroundExecutor.getInstance().execute(new WithdrawWizchipsRequester(programOrLab.getId(), selectted, 1));
                    unCheckAllStudents();
                }
                break;
            default:
                break;
        }

    }

    private int getChips(int onlineChips, int offlineChips) {
        return (onlineChips + offlineChips) > 0 ? (onlineChips + offlineChips) : 0;
    }

    private ArrayList<Student> getSelectedStudents() {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        for (Object object : objectArrayList) {
            if (((Student) object).isCheck()) {
                studentArrayList.add((Student) object);
            }
        }
        return studentArrayList;
    }

    private ArrayList<Student> unCheckedAllStudents() {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        for (Object object : objectArrayList) {
            if (((Student) object).isCheck()) {
                studentArrayList.add((Student) object);
            }
        }
        return studentArrayList;
    }


    @Override
    public void markAbsentSuccessful(final ArrayList<Student> studentArrayList, final String date) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dateSelected = null;
                notifyLabDetailsAdapter();
                if (studentArrayList != null) {
                    if (!studentArrayList.isEmpty() && studentArrayList.size() > 1) {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_ARE_MARKED_ABSENT_SUCCESSFULLY_FOR + date);
                    } else {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_MARKED_ABSENT_SUCCESSFULLY_FOR + date);
                    }
                }
            }
        });
    }

    @Override
    public void markAbsentUnSuccessful(final int status) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dateSelected = null;
                notifyLabDetailsAdapter();
                if (status == 1001) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_ALREADY_MARKED_ABSENT_FOR_SELECTED_DATE);
                } else if (status == 1002) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_ARE_ALREADY_MARKED_ABSENT_FOR_SELECTED_DATE);
                } else if (status != 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.OOPS_SOMETHING_WENT_WRONG);
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    @Override
    public void promotionDemotionSuccessful(final ArrayList<Student> student, Program program, final boolean promote) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyLabDetailsAdapter();
                if (promote) {
                    if (student != null) {
                        if (!student.isEmpty() && student.size() > 1) {
                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_PROMOTED_SUCCESSFULLY);
                        } else {
                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_PROMOTED_SUCCESSFULLY);
                        }
                    }
                } else {
                    if (!student.isEmpty() && student.size() > 1) {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_DEMOTED_SUCCESSFULLY);
                    } else {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_DEMOTED_SUCCESSFULLY);
                    }
                }
            }
        });
    }

    @Override
    public void promotionDemotionUnSuccessful(final int status, final ArrayList<Student> student, Program program, final boolean promote) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyLabDetailsAdapter();
                if (status == 5000) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_ALREADY_AT_HIGHEST_LEVEL_AVAILABLE);
                } else if (status == 6000) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_ALREADY_AT_LOWEST_LEVEL_AVAILABLE);
                } else if (status == 7000) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_ARE_ALREADY_AT_HIGHEST_LEVEL_AVAILABLE);
                } else if (status == 8000) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_ARE_ALREADY_AT_LOWEST_LEVEL_AVAILABLE);
                }/* else if (status != 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.OOPS_SOMETHING_WENT_WRONG);
                } */ else {
//                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
                    if (promote) {
                        if (student != null) {
                            if (!student.isEmpty() && student.size() > 1) {
                                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_PROMOTED_SUCCESSFULLY);
                            } else {
                                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_PROMOTED_SUCCESSFULLY);
                            }
                        }
                    } else {
                        if (!student.isEmpty() && student.size() > 1) {
                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENTS_DEMOTED_SUCCESSFULLY);
                        } else {
                            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.STUDENT_IS_DEMOTED_SUCCESSFULLY);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onAddWizchipsSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyLabDetailsAdapter();
            }
        });
    }

    @Override
    public void onAddWizchipsError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyLabDetailsAdapter();
            }
        });
    }

    @Override
    public void onWithdrawWizchipsSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyLabDetailsAdapter();
            }
        });
    }

    @Override
    public void onWithdrawWizchipsError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyLabDetailsAdapter();
            }
        });
    }

    private void notifyLabDetailsAdapter() {
        objectArrayList.clear();
        preservePreviousCheckStatus();
        labDetailsAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void onSyncDone() {
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean syncStatus = SyncManager.getInstance().isLabDetailSynced();
                Log.d("LabDetailsFragment", "onSyncDone " + syncStatus);
                updateSyncStatus(syncStatus);
            }
        });
    }

    private void updateSyncStatus(boolean isSync) {
        if (isSync) {
            labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        } else {
            labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);
        }
    }

    private void maintnedCheckedStudentsStatus() {
        status = new boolean[objectArrayList.size()];
        for (int i = 0; i < objectArrayList.size(); i++) {
            status[i] = false;
        }
    }

    private void preservePreviousCheckStatus() {

        objectArrayList.addAll(ProgramStudentsTable.getInstance().getStudentsListByProgramId(programOrLab.getId()));
        if (status == null) {
            maintnedCheckedStudentsStatus();
        }
        for (int i = 0; i < status.length; i++) {
            if (status[i]) {
                ((Student) objectArrayList.get(i)).setCheck(status[i]);
            }
        }
    }

    private void unCheckAllStudents(){

        for(int i =0; i<objectArrayList.size();i++){
            ((Student)objectArrayList.get(i)).setCheck(false);
        }
        maintnedCheckedStudentsStatus();
        labDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void notHavePermissionForPromotionDemotion(final String message) {
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(homeActivityContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void notHavePermissionForWizchips(final String message) {
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(homeActivityContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void notHavePermissionToWithdraw(final String message) {
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(homeActivityContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
