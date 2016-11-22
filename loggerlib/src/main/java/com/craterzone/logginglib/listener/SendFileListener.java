package com.craterzone.logginglib.listener;

import com.craterzone.logginglib.model.DiagnosticResponse;

public interface SendFileListener {
    void onSendFileSuccess(DiagnosticResponse response);

    void onSendFileError(DiagnosticResponse response);
}
