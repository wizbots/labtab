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
        String NOVICE = "NOVICE";
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
        String NO_DETAIL_FOUND = "No Detail Found";
        String STUDENT_STATISTICS = "Student Statistics";
        String MENTOR_NOT_FOUND = "Mentor Not Found";
        String NO_LAB_DETAIL_FOR_THIS_LAB = "No Lab Detail for this Lab";
        String NO_STUDENT_FOUND_FOR_THIS_LAB = "No Student Found for this Lab";
        String NO_ADDITIONAL_INFO_FOUND_FOR_THIS_LAB = "No Additional Info found for this Lab";
        String NO_LIST_OF_SKIPS_FOUND_FOR_THIS_LAB = "No List of Skips found for this Lab";
        String AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_ADD_VIDEO_FOR_THIS_LAB = "At least one student is needed to add video for this lab";
        String AT_LEAST_ONE_STUDENT_IS_NEEDED_FOR_PROMOTION_FOR_THIS_LAB = "At least one student is needed for promotion for this lab";
        String AT_LEAST_ONE_STUDENT_IS_NEEDED_FOR_DEMOTION_FOR_THIS_LAB = "At least one student is needed for demotion for this lab";
        String AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_MARK_ABSENT_FOR_THIS_LAB = "At least one student is needed to mark absent for this lab";
        String PLEASE_SELECT_AT_LEAST_ONE_STUDENT_TO_MARK_ABSENT = "Please Select at least one student to mark absent";
        String PLEASE_SELECT_AT_LEAST_ONE_STUDENT_TO_PROMOTE = "Please Select at least one student to promote";
        String PLEASE_SELECT_AT_LEAST_ONE_STUDENT_TO_DEMOTE = "Please Select at least one student to demote";
        String STUDENTS_ARE_MARKED_ABSENT_SUCCESSFULLY_FOR = "Students are Marked Absent Successfully for ";
        String STUDENT_IS_MARKED_ABSENT_SUCCESSFULLY_FOR = "Student is Marked Absent Successfully for ";
        String STUDENTS_PROMOTED_SUCCESSFULLY = "Students Promoted Successfully";
        String STUDENTS_DEMOTED_SUCCESSFULLY = "Students Demoted Successfully";
        String STUDENT_IS_PROMOTED_SUCCESSFULLY = "Student is Promoted Successfully";
        String STUDENT_IS_DEMOTED_SUCCESSFULLY = "Student is Demoted Successfully";
        String OOPS_SOMETHING_WENT_WRONG = "Oops Something Went Wrong";
        String PROJECT_EDITED_SUCCESSFULLY = "Project Edited Successfully";
        String VIDEO_CAN_NOT_BE_EDITED_WHEN_IT_IS_NOT_UPLOADED_TO_WIZBOTS_SERVER = "Video can't be edited when it is not uploaded to wizbots server";
        String SELECT_STUDENT_FIRST = "Select Student First";
        String PLEASE_SELECT_AT_MOST_ONE_STUDENT_TO_WIZCHIPS= "Please Select one student";
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

    interface DrawerItem {
        String ITEM_GO_TO = "Go to www.wizbots.com";
        String ITEM_LAB_LIST = "Lab List";
        String ITEM_VIDEO_LIST = "Video List";
        String ITEM_LOGOUT = "Logout";
    }

    interface Title {
        String ADDITIONAL_INFORMATION = "Additional Information";
        String ADD_VIDEO = "Add Video";
        String EDIT_VIDEO = "Edit Video";
        String FORGOT_PASSWORD = "Please enter your email";
        String LAB_DETAILS = "Lab Details";
        String LAB_LIST = "Lab List";
        String LIST_OF_SKIPS = "List Of Skips";
        String LOGIN = "Please enter your name and password";
        String MENTOR_PROFILE = "My Account";
        String STUDENT_LAB_DETAILS = "Lab Details";
        String STUDENT_PROFILE = "Kid Name Account";
        String STUDENT_STATS_DETAILS = "Lab Details";
        String VIDEO_LIST = "Video List";
        String VIEW_VIDEO = "View Video";
    }

    interface DateFormat {
        String DEFAULT = "dd/MM/yyyy";
        String YYYYMMDD = "yyyy-MM-dd";
    }
}
