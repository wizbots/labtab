package org.wizbots.labtab;

import java.util.regex.Pattern;

public interface LabTabConstants {

    //Web Services Status Codes

    int SC_ACCEPTED = 202;
    int SC_BAD_GATEWAY = 502;
    int SC_BAD_REQUEST = 400;
    int SC_CONFLICT = 409;
    int SC_CONTINUE = 100;
    int SC_CREATED = 201;
    int SC_EXPECTATION_FAILED = 417;
    int SC_FAILED_DEPENDENCY = 424;
    int SC_FORBIDDEN = 403;
    int SC_GATEWAY_TIMEOUT = 504;
    int SC_GONE = 410;
    int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
    int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    int SC_INSUFFICIENT_STORAGE = 507;
    int SC_INTERNAL_SERVER_ERROR = 500;
    int SC_LENGTH_REQUIRED = 411;
    int SC_LOCKED = 423;
    int SC_METHOD_FAILURE = 420;
    int SC_METHOD_NOT_ALLOWED = 405;
    int SC_MOVED_PERMANENTLY = 301;
    int SC_MOVED_TEMPORARILY = 302;
    int SC_MULTIPLE_CHOICES = 300;
    int SC_MULTI_STATUS = 207;
    int SC_NON_AUTHORITATIVE_INFORMATION = 203;
    int SC_NOT_ACCEPTABLE = 406;
    int SC_NOT_FOUND = 404;
    int SC_NOT_IMPLEMENTED = 501;
    int SC_NOT_MODIFIED = 304;
    int SC_NO_CONTENT = 204;
    int SC_OK = 200;
    int SC_PARTIAL_CONTENT = 206;
    int SC_PAYMENT_REQUIRED = 402;
    int SC_PRECONDITION_FAILED = 412;
    int SC_PROCESSING = 102;
    int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    int SC_REQUEST_TIMEOUT = 408;
    int SC_REQUEST_TOO_LONG = 413;
    int SC_REQUEST_URI_TOO_LONG = 414;
    int SC_RESET_CONTENT = 205;
    int SC_SEE_OTHER = 303;
    int SC_SERVICE_UNAVAILABLE = 503;
    int SC_SWITCHING_PROTOCOLS = 101;
    int SC_TEMPORARY_REDIRECT = 307;
    int SC_UNAUTHORIZED = 401;
    int SC_UNPROCESSABLE_ENTITY = 422;
    int SC_UNSUPPORTED_MEDIA_TYPE = 415;
    int SC_USE_PROXY = 305;

    //Splash Delay

    int DELAY_MILLIS = 1000;

    //LabTab Fragments

    int FRAGMENT_LOGIN = 0;
    int FRAGMENT_FORGOT_PASSWORD = 1;
    int FRAGMENT_HOME = 2;
    int FRAGMENT_LAB_LIST = 3;
    int FRAGMENT_LAB_DETAILS_LIST = 4;
    int FRAGMENT_MENTOR_PROFILE = 5;
    int FRAGMENT_STUDENT_PROFILE = 6;
    int FRAGMENT_STUDENT_STATS_DETAILS = 7;
    int FRAGMENT_STUDENT_LAB_DETAILS = 8;
    int FRAGMENT_VIDEO_LIST = 9;
    int FRAGMENT_EDIT_VIDEO = 10;
    int FRAGMENT_ADD_VIDEO = 11;
    int FRAGMENT_LIST_OF_SKIPS = 12;
    int FRAGMENT_ADDITIONAL_INFORMATION = 13;
    int FRAGMENT_VIEW_VIDEO = 14;


    //Lab Levels

    String LAB_LEVEL_APPRENTICE = "APPRENTICE";
    String LAB_LEVEL_EXPLORER = "EXPLORER";
    String LAB_LEVEL_IMAGINEER = "IMAGINEER";
    String LAB_LEVEL_LAB_CERTIFIED = "LAB_CERTIFIED";
    String LAB_LEVEL_MAKER = "MAKER";
    String LAB_LEVEL_MASTER = "MASTER";
    String LAB_LEVEL_WIZARD = "WIZARD";

    //Lab MARKS

    String MARKS_NONE = "";
    String MARKS_DONE = "DONE";
    String MARKS_SKIPPED = "SKIPPED";
    String MARKS_PENDING = "PENDING";
    String MARKS_IMAGINEERING = "IMAGINEERING";
    String MARKS_PROGRAMMING = "PROGRAMMING";
    String MARKS_MECHANISMS = "MECHANISMS";
    String MARKS_STRUCTURES = "STRUCTURES";
    String MARKS_CLOSE_TO_NEXT_LEVEl = "CLOSE_TO_NEXT_LEVEL";

    //Lab Steps

    String LAB_STEP_1 = "STEP_1";
    String LAB_STEP_2 = "STEP_2";
    String LAB_STEP_3 = "STEP_3";
    String LAB_STEP_4 = "STEP_4";

    //Toast texts


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
    String FINISH = "finish";


    /**
     * Email validation pattern.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

}
