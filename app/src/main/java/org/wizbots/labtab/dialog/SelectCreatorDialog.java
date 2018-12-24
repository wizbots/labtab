package org.wizbots.labtab.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.wizbots.labtab.R;
import org.wizbots.labtab.adapter.ProjectCreatorAdapter;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class SelectCreatorDialog extends Dialog {
    private ArrayList<Student> studentArrayList;
    private ArrayList<Student> selectedStudentArrayList;
    private ArrayList<Student> initialSelectedStudentArrayList;
    private SelectedCreatorDialogListener listener;
    private RecyclerView creatorList;
    private EditText search;
    private Activity context;

    public SelectCreatorDialog(@NonNull Activity context, ArrayList<Student> studentArrayList, ArrayList<Student> selectedStudentArrayList, SelectedCreatorDialogListener listener) {
        super(context);
        this.context = context;
        this.studentArrayList = studentArrayList;
        this.initialSelectedStudentArrayList = selectedStudentArrayList;
        this.selectedStudentArrayList = new ArrayList<>();
        this.selectedStudentArrayList.addAll(initialSelectedStudentArrayList);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_creator);
        search = (EditText) findViewById(R.id.et_search);
        setCancelable(false);
        creatorList = (RecyclerView) findViewById(R.id.rv_creator_list);
        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final ArrayList<Student> filteredList = new ArrayList<>();

                for (int i = 0; i < studentArrayList.size(); i++) {

                    final String text = ((studentArrayList.get(i)).getName().toLowerCase());
                    if (text.contains(query)) {
                        filteredList.add(studentArrayList.get(i));
                    }
                }

                creatorList.setAdapter(new ProjectCreatorAdapter(filteredList,selectedStudentArrayList));
            }
        });
        creatorList.setAdapter(new ProjectCreatorAdapter(studentArrayList,selectedStudentArrayList));
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.clearFocus();
                LabTabUtil.hideSoftKeyboard(context);
                dismiss();
                initialSelectedStudentArrayList.clear();
                if (!selectedStudentArrayList.isEmpty()){
                    initialSelectedStudentArrayList.addAll(selectedStudentArrayList);
                }
                listener.onSaveClick();

            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.clearFocus();
                LabTabUtil.hideSoftKeyboard(context);
                dismiss();
            }
        });
    }

    public interface SelectedCreatorDialogListener{
        void onSaveClick();
    }
}
