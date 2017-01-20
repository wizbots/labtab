package org.wizbots.labtab;

import java.util.regex.Pattern;

public interface LabTabConstants {

    //Web Services Status Codes

    interface StatusCode {
        int ACCEPTED = 202;
        int BAD_GATEWAY = 502;
        int BAD_REQUEST = 400;
        int CONFLICT = 409;
        int CONTINUE = 100;
        int CREATED = 201;
        int EXPECTATION_FAILED = 417;
        int FAILED_DEPENDENCY = 424;
        int FORBIDDEN = 403;
        int GATEWAY_TIMEOUT = 504;
        int GONE = 410;
        int HTTP_VERSION_NOT_SUPPORTED = 505;
        int INSUFFICIENT_SPACE_ON_RESOURCE = 419;
        int INSUFFICIENT_STORAGE = 507;
        int INTERNAL_SERVER_ERROR = 500;
        int LENGTH_REQUIRED = 411;
        int LOCKED = 423;
        int METHOD_FAILURE = 420;
        int METHOD_NOT_ALLOWED = 405;
        int MOVED_PERMANENTLY = 301;
        int MOVED_TEMPORARILY = 302;
        int MULTIPLE_CHOICES = 300;
        int MULTI_STATUS = 207;
        int NON_AUTHORITATIVE_INFORMATION = 203;
        int NOT_ACCEPTABLE = 406;
        int NOT_FOUND = 404;
        int NOT_IMPLEMENTED = 501;
        int NOT_MODIFIED = 304;
        int NO_CONTENT = 204;
        int OK = 200;
        int PARTIAL_CONTENT = 206;
        int PAYMENT_REQUIRED = 402;
        int PRECONDITION_FAILED = 412;
        int PROCESSING = 102;
        int PROXY_AUTHENTICATION_REQUIRED = 407;
        int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
        int REQUEST_TIMEOUT = 408;
        int REQUEST_TOO_LONG = 413;
        int REQUEST_URI_TOO_LONG = 414;
        int RESET_CONTENT = 205;
        int SEE_OTHER = 303;
        int SERVICE_UNAVAILABLE = 503;
        int SWITCHING_PROTOCOLS = 101;
        int TEMPORARY_REDIRECT = 307;
        int UNAUTHORIZED = 401;
        int UNPROCESSABLE_ENTITY = 422;
        int UNSUPPORTED_MEDIA_TYPE = 415;
        int USE_PROXY = 305;
    }

    //Splash Delay

    int DELAY_MILLIS = 1000;

    //LabTab Fragments

    interface Fragments {
        int LOGIN = 0;
        int FORGOT_PASSWORD = 1;
        int HOME = 2;
        int LAB_LIST = 3;
        int LAB_DETAILS_LIST = 4;
        int MENTOR_PROFILE = 5;
        int STUDENT_PROFILE = 6;
        int STUDENT_STATS_DETAILS = 7;
        int STUDENT_LAB_DETAILS = 8;
        int VIDEO_LIST = 9;
        int EDIT_VIDEO = 10;
        int ADD_VIDEO = 11;
        int LIST_OF_SKIPS = 12;
        int ADDITIONAL_INFORMATION = 13;
        int VIEW_VIDEO = 14;
    }

    //Lab Levels

    interface LabLevels {
        String APPRENTICE = "APPRENTICE";
        String EXPLORER = "EXPLORER";
        String IMAGINEER = "IMAGINEER";
        String LAB_CERTIFIED = "LAB CERTIFIED";
        String MAKER = "MAKER";
        String MASTER = "MASTER";
        String WIZARD = "WIZARD";
    }

    //Lab MARKS

    interface Marks {
        String NONE = "";
        String DONE = "DONE";
        String SKIPPED = "SKIPPED";
        String PENDING = "PENDING";
        String IMAGINEERING = "IMAGINEERING";
        String PROGRAMMING = "PROGRAMMING";
        String MECHANISMS = "MECHANISMS";
        String STRUCTURES = "STRUCTURES";
        String CLOSE_TO_NEXT_LEVEl = "CLOSE_TO_NEXT_LEVEL";
    }

    //Lab Steps

    interface Steps {
        String LAB_STEP_1 = "STEP_1";
        String LAB_STEP_2 = "STEP_2";
        String LAB_STEP_3 = "STEP_3";
        String LAB_STEP_4 = "STEP_4";
    }

    //Toast texts

    interface ToastTexts {
        String PROVIDED_PASSWORD_OR_USERNAME_ARE_INVALID = "Provided password or username are invalid";
        String NOT_USER_WITH_PROVIDED_CREDENTIALS = "Not user with provided credentials";
        String NO_INTERNET_CONNECTION = "No Internet Connection";
        String LOGIN_SUCCESSFULL = "Login Successfull";
        String PLEASE_ENTER_EMAIL = "Please Enter Email";
        String PLEASE_ENTER_VALID_EMAIL_ADDRESS = "Please Enter Valid Email Address";
        String PLEASE_ENTER_PASSWORD = "Please Enter Password";
        String GO_TO_WIZBOTS_COM = "http://wizbots.com";
        String FEATURE_NOT_AVAILABLE_RIGHT_NOW = "Feature Not Available Right Now";
        String NO_LAB_FOUND = "No Lab Found";
        String STUDENT_STATISTICS = "Student Statistics";
        String MENTOR_NOT_FOUND = "Mentor Not Found";
        String NO_LAB_DETAIL_FOR_THIS_LAB = "No Lab Detail for this Lab";
        String NO_STUDENT_FOUND_FOR_THIS_LAB = "No Student Found for this Lab";
        String NO_ADDITIONAL_INFO_FOUND_FOR_THIS_LAB = "No Additional Info found for this Lab";
        String NO_LIST_OF_SKIPS_FOUND_FOR_THIS_LAB = "No List of Skips found for this Lab";
        String AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_ADD_VIDEO = "At least one student is needed to add video";
    }

    /**
     * Email validation pattern.
     */
    Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


    //Events for LabTab Upload Service
    interface Events {
        String DEVICE_CONNECTED_TO_INTERNET = "DEVICE_CONNECTED_TO_INTERNET";
        String ADD_VIDEO = "ADD_VIDEO";
        String DEVICE_REBOOTED = "DEVICE_REBOOTED";
        String USER_LOGGED_IN = "USER_LOGGED_IN";
        String VIDEO_LIST = "VIDEO_LIST";
        String NO_EVENT = "NO_EVENT";
        String DEFAULT = "DEFAULT";
    }

    interface Constants {
        String FINISH = "finish";
    }

    interface Action {
        String MAIN_ACTION = "org.wizbots.labtab.action.main";
        String START_FOREGROUND_ACTION = "org.wizbots.labtab.action.startforeground";
        String STOP_FOREGROUND_ACTION = "org.wizbots.labtab.action.stopforeground";
    }

    int FOREGROUND_SERVICE = 500;

}
