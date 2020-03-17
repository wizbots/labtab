package org.wizbots.labtab;

import java.util.regex.Pattern;

public interface LabTabConstants {

    //Web Services Status Codes

    interface StatusCode {
        int NO_INTERNET = 0;
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

        int LAB_LIST_DRAWER = 15;
        int VIDEO_LIST_DRAWER = 16;
        int ADD_VIDEO_DRAWER = 16;

        int BINDER = 21;
        int SETTINGS = 22;
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
        String NO_DATA_FOUND = "No Data Found";
        String NO_DETAIL_FOUND = "No Detail Found";
        String STUDENT_STATISTICS = "Student Statistics";
        String MENTOR_NOT_FOUND = "Mentor Not Found";
        String USER_IS_NOT_MENTOR = "User is not mentor";
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
        String NO_CONDITION_MATCH="Internal error occured, Please contact support and send logs to help troubleshoot the issue";
        String PROJECT_EDITED_SUCCESSFULLY = "Project Edited Successfully";
        String VIDEO_CAN_T_BE_EDITED_WHILE_UPLOADING = "Video can't be edited while uploading";
        String SELECT_STUDENT_FIRST_INCREMENT = "Please select one student to increase wizchips";
        String ALREADY_MINIMUM_WIZCHIPS = "Student is already having lowest wizchips";
        String SELECT_STUDENT_FIRST_DECREMENT = "Please select one student to descrease wizchips";
        String YOU_CAN_NOT_SELECT_DATE_MORE_THAN_TODAY = "You can not select a date more than today";
        String ALREADY_MINIMUM = "Student is already having lowest wizchips";
        String STUDENT_IS_ALREADY_MARKED_ABSENT_FOR_SELECTED_DATE = "Student is already marked absent for the selected date";
        String STUDENTS_ARE_ALREADY_MARKED_ABSENT_FOR_SELECTED_DATE = "Students are already marked absent for the selected date";
        String STUDENT_IS_ALREADY_AT_LOWEST_LEVEL_AVAILABLE = "Student is already at lowest level (Novice)";
        String STUDENT_IS_ALREADY_AT_HIGHEST_LEVEL_AVAILABLE = "Student is already at highest level (Master)";
        String STUDENTS_ARE_ALREADY_AT_LOWEST_LEVEL_AVAILABLE = "Students are already at lowest level (Novice)";
        String STUDENTS_ARE_ALREADY_AT_HIGHEST_LEVEL_AVAILABLE = "Students are already at highest level (Master)";
        String NO_CHANGES_ARE_MADE = "No changes are made";
        String VIDEO_CAN_T_BE_EDITED_WHILE_UPDATING = "Video can't be edited while updating";
        String PROJECT_CREATED_SUCCESSFULLY = "Project created successfully, scheduled for uploading";
        String STUDENT_IS_ALREADY_PROMOTED_DEMOTED_BY_ONE_LEVEL = "Student is already promoted/demoted by one level";
        String PROJECT_DELETED_SUCCESS = "Project Deleted Successfully";
        String PROJECT_DELETED_FAILED = "Project failed to delete";
        String NO_DATA_NO_CONNECTION = "No data available, please try again later with Internet connectivity";
        String PLEASE_SELECT_DATE_FIRST = "Please select date first";
        String FAILED_TO_FETCH_FILTER = "Failed to fetch filter";
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
        String DEVICE_DISCONNECTED_TO_INTERNET = "DEVICE_DISCONNECTED_TO_INTERNET";
        String ADD_VIDEO = "ADD_VIDEO";
        String DEVICE_REBOOTED = "DEVICE_REBOOTED";
        String USER_LOGGED_IN = "USER_LOGGED_IN";
        String VIDEO_LIST = "VIDEO_LIST";
        String NO_EVENT = "NO_EVENT";
        String DEFAULT = "DEFAULT";
        String LAB_DETAIL_LIST = "LAB_DETAIL_LIST";
    }

    interface Constants {
        String FINISH = "finish";
        int PERMISSION_REQUEST_CODE = 100;
        int PERMISSION_REQUEST_CODE_STORAGE = 101;
    }

    interface Action {
        String MAIN_ACTION = "org.wizbots.labtab.action.main";
        String START_FOREGROUND_ACTION = "org.wizbots.labtab.action.startforeground";
        String STOP_FOREGROUND_ACTION = "org.wizbots.labtab.action.stopforeground";
    }

    int FOREGROUND_SERVICE = 500;

    interface DrawerItem {
        String ITEM_GO_TO = "Go to www.wizbots.com";
        String ITEM_BINDER = "Binder";
        String ITEM_LAB_LIST = "Lab List";
        String ITEM_VIDEO_LIST = "Video List";
        String ITEM_ADD_VIDEO = "Add Video";
        String ITEM_SETTINGS = "Settings";
        String ITEM_LOGOUT = "Logout";
        String ITEM_USER_PROFILE = "User Profile";
        String ITEM_ROSTER = "Roster";
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
        String MMDDYY = "MM/dd/yy";
    }

    interface SyncStatus {
        String SYNCED = "SYNCED";
        String NOT_SYNCED = "NOT_SYNCED";
        String PROMOTION_NOT_SYNCED = "PROMOTION_NOT_SYNCED";
        String DEMOTION_NOT_SYNCED = "DEMOTION_NOT_SYNCED";
        String PROMOTION_DEMOTION_SYNCED = "PROMOTION_DEMOTION_SYNCED";
    }

    interface ProjectsMetaData {
        String CONTENT = "[\n" +
                "  {\n" +
                "    \"index\": 1,\n" +
                "    \"name\": \"Lab Certified\",\n" +
                "    \"color1\": \"white\",\n" +
                "    \"color2\": \"lightsteelblue\",\n" +
                "    \"nuggets\": [\n" +
                "      \"Tool Chain - 1\",\n" +
                "      \"Motor Control\",\n" +
                "      \"NXT Button Control\",\n" +
                "      \"Touch Sensor Control\"\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      \"Tick Tock\",\n" +
                "      \"Hypnotizer\",\n" +
                "      \"Chariot\",\n" +
                "      \"Obstacle Course\",\n" +
                "      \"Build Strong\",\n" +
                "      \"Castles and Catapults\",\n" +
                "      \"The Wizbots Way\",\n" +
                "      \"MadLib\"\n" +
                "    ],\n" +
                "    \"wiz_chips\": 10\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 2,\n" +
                "    \"name\": \"Explorer\",\n" +
                "    \"color1\": \"purple\",\n" +
                "    \"color2\": \"lightsteelblue\",\n" +
                "    \"nuggets\": [\n" +
                "      \"Wait Commands\",\n" +
                "      \"Flow Control - 1\",\n" +
                "      \"Speaker Control\",\n" +
                "      \"Display Control\",\n" +
                "      \"Tool Chain - 2\"\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      \"Windscreen Wiper\",\n" +
                "      \"Bumper Car\",\n" +
                "      \"Hello World!\",\n" +
                "      \"Sound and Music\",\n" +
                "      \"Plastic Fabtastic\",\n" +
                "      \"Tallest Robot\",\n" +
                "      \"LED Wand\",\n" +
                "      \"Dancing Puppets\",\n" +
                "      \"Parallerl Programming\",\n" +
                "      \"Diff Drive Control\",\n" +
                "      \"Wizball\"\n" +
                "    ],\n" +
                "    \"wiz_chips\": 10\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 3,\n" +
                "    \"name\": \"Apprentice\",\n" +
                "    \"color1\": \"#324596\",\n" +
                "    \"color2\": \"lightsteelblue\",\n" +
                "    \"nuggets\": [\n" +
                "      \"Distance Sensor Control\",\n" +
                "      \"Sound Sensor Control\",\n" +
                "      \"Light Sensor Control\",\n" +
                "      \"LED Wand\",\n" +
                "      \"Color Sensor Control\"\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      \"Troubleshooting\",\n" +
                "      \"Wheel Free Racing\",\n" +
                "      \"Distance Sensor\",\n" +
                "      \"Table Top Survival\",\n" +
                "      \"Sound Sensor\",\n" +
                "      \"Barking Dog\",\n" +
                "      \"Light Sensor\",\n" +
                "      \"Sun Flower\",\n" +
                "      \"Color Sensor\",\n" +
                "      \"Red Light Green Light\",\n" +
                "      \"Random Numbers\",\n" +
                "      \"Floor Sweeper\"\n" +
                "    ],\n" +
                "    \"wiz_chips\": 15\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 4,\n" +
                "    \"name\": \"Maker\",\n" +
                "    \"color1\": \"#C84239\",\n" +
                "    \"color2\": \"lightsteelblue\",\n" +
                "    \"nuggets\": [\n" +
                "      \"Parallel Programming\",\n" +
                "      \"Flow Control - 2\",\n" +
                "      \"Line Following\",\n" +
                "      \"Play Station Control - 1\"\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      \"Line Following\",\n" +
                "      \"Line Racing\",\n" +
                "      \"Using The Force\",\n" +
                "      \"Drag Racing\",\n" +
                "      \"Hoist\",\n" +
                "      \"Elevator\",\n" +
                "      \"Screw Gear\",\n" +
                "      \"Slow Poke\"\n" +
                "    ],\n" +
                "    \"wiz_chips\": 15\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 5,\n" +
                "    \"name\": \"Imagineer\",\n" +
                "    \"color1\": \"#1B7BA4\",\n" +
                "    \"color2\": \"lightsteelblue\",\n" +
                "    \"nuggets\": [\n" +
                "      \"Variables\",\n" +
                "      \"Tool Chain - 3\",\n" +
                "      \"Arithmetic Relations\",\n" +
                "      \"Rotation Sensor Control\",\n" +
                "      \"Gyroscope Sensor Control\",\n" +
                "      \"Compass Sensor Control\",\n" +
                "      \"Magnetic Sensor Control\",\n" +
                "      \"Optical Distance Sensor Control\",\n" +
                "      \"Play Station Control - 2\"\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      \"Counting\",\n" +
                "      \"Boolean Logic\",\n" +
                "      \"Rotation Sensor\",\n" +
                "      \"Etch-A-Sketch\",\n" +
                "      \"Compass Sensor\",\n" +
                "      \"North Pole Express\",\n" +
                "      \"Slider Crank\",\n" +
                "      \"Magnetic Sensor\",\n" +
                "      \"Shell Game\",\n" +
                "      \"Ackerman\",\n" +
                "      \"Steering\"\n" +
                "    ],\n" +
                "    \"wiz_chips\": 15\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 6,\n" +
                "    \"name\": \"Wizard\",\n" +
                "    \"color1\": \"green\",\n" +
                "    \"color2\": \"lightsteelblue\",\n" +
                "    \"nuggets\": [\n" +
                "      \"Flow Control - 3\",\n" +
                "      \"Boolean Operators\",\n" +
                "      \"Accelerometer Sensor Control\",\n" +
                "      \"Camera Sensor Control\",\n" +
                "      \"File Input and Output\",\n" +
                "      \"Math Functions\",\n" +
                "      \"String manipulation\"\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      \"Algorithms\",\n" +
                "      \"Gyroscope\",\n" +
                "      \"Data Structures\",\n" +
                "      \"Accelerometer\",\n" +
                "      \"Camera\"\n" +
                "    ],\n" +
                "    \"wiz_chips\": 15\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 7,\n" +
                "    \"name\": \"Master\",\n" +
                "    \"color1\": \"orange\",\n" +
                "    \"color2\": \"lightsteelblue\",\n" +
                "    \"nuggets\": [\n" +
                "      \"Advanced Data Structures\",\n" +
                "      \"Basic Object Oriented Progamming\",\n" +
                "      \"Behavior-based Control\",\n" +
                "      \"PID Control\"\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      \"Objects\",\n" +
                "      \"Networking\",\n" +
                "      \"Behaviors\",\n" +
                "      \"PID Control\",\n" +
                "      \"Segway\"\n" +
                "    ],\n" +
                "    \"wiz_chips\": 15\n" +
                "  }\n" +
                "]";
    }

    interface FilterRequestParameter {
        String CITY = "city";
        String FROM = "from";
        String SEASON = "season";
        String TO = "to";
        String SEASON_YEAR = "season_year";
        String LOCATION_ID = "location_id";
        String MENTOR_ID = "mentor_id";
    }

    interface VideoEditCase {
        String INTERNET_ON = "INTERNET_ON";
        String INTERNET_OFF = "INTERNET_OFF";
    }

    interface Screens {
        String FROM_SCREEN = "from_screen";
        String ROSTER_DETAILS = "roster_details";
        String SCREEN_NONE = "screen_none";
    }

    String VIDEO_LOGS_TAG = "VIDEO_LOGS";
}
