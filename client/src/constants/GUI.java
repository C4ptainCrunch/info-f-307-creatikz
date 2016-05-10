package constants;

import utils.Dirs;

import java.awt.*;
import java.util.*;

public final class GUI {
    public static final class MenuBar {
        public static String FILE_MENU = "File";
        public static String SAVE = "Save";
        public static String PDF = "Build PDF";
        public static String EXIT = "Exit";
        public static String VIEW_MENU = "View";
        public static String GRID_VISIBILTY = "Show/Hide Grid";
        public static String HELP_MENU = "Help";
        public static String HELP = "See help panel";
        public static String DIFF = "Show History";
        public static String APP_NAME = "CreaTikZ";
        public static String OPTIONS_MENU = "Options";
        public static String COLOR_BLIND = "ColorBlind mode";
        public static String EDIT_MENU = "Edit";
        public static String UNDO = "Undo";
        public static String REDO = "Redo";
    }

    public static final class ProjectManagement {

        public static final String CREATE_BUTTON = "Create";
        public static final String OPEN_BUTTON = "Open";
        public static final String RENAME_BUTTON = "Move project";
        public static final String CREATE_PANEL = "Choose location to create your project";
        public static final String IMPORT_PANEL = "Choose location to import your project";
        public static final String DROPDOWN_HEADER = "Choose existing project from this list" + " and press 'Open' or 'Rename'.";
        public static final String BLANK_INFO_PANEL = "INFORMATION ABOUT SELECTED PROJECT:\nDiagram Name: %s"
                + "\nUser: %s\nLast revision: %s\n";
        public static String DIFF_TEXT = "Diff History";
    }

    public static final class LoginWindow {
        public static String LOGIN_BUTTON = "Login";
        public static String SIGNUP_BUTTON = "Sign Up";
        public static String TOKEN_BUTTON = "Token Activation";
        public static String EDIT_BUTTON = "Edit profile";

    }

    public static final class TokenWindow {
        public static String WIN_LABEL = "An email has been sent to you. Please copy here the" +
                                           " token enclosed within the email and press OK.";
        public static String OK_BUTTON = "OK";
        public static String USER_LABEL = "Username: ";
        public static String TOKEN_LABEL = "Token: ";
        public static String TOKEN_VALID = "Congratulations! Your account is now validated.\\nYou may log in now.";
        public static String TOKEN_WRONG = "That token is wrong. Please make sure you have copied it well.";
    }

    public static final class SignUp {
        public static ArrayList<String> FIELD_LABELS = new ArrayList<String>(){{
            add("First Name: ");
            add("Last Name: ");
            add("Username: ");
            add("Email: ");
        }};

        public static ArrayList<Integer> FIELD_SIZES = new ArrayList<Integer>(){{
            add(16);
            add(32);
            add(16);
            add(32);
        }};

        public static String PASSWORD_LABEL = "Password: ";
        public static String OK_BUTTON = "OK";
        public static String CANCEL_BUTTON = "Cancel";

        public static String NAMES_REGEX = "^[\\p{L} .'-]+$";
        public static String USERNAME_REGEX = "[A-Za-z0-9]+";

    }

    public static final class Drawing {
        public static double ARROW_LENGTH = 30;
        public static double ARROW_ANGLE = Math.PI / 6;
    }

    public static final class Tabs {
        public static String SHAPE_TAB = "<html>S<br>H<br>A<br>P<br>E<br>S</html>";
        public static String TEMPLATE_TAB = "<html>T<br>E<br>M<br>P<br>L<br>A<br>T<br>E<br>S</html>";
    }

    public static final class Drag {
        public enum DropOptions {
            MOVE, ADD;
        }
    }

    public static final class Selection {
        public static Color BKG_COLOR = new Color(0, 50, 120, 50);
    }

    public static final class Template {
        public static String DIR = Dirs.getDataDir().resolve("templates").toString();
    }

    public static final class TextArea {
        public static String DEFAULT_THEME = "syntax_themes/default.xml";
        public static String DEFAULT_COLOR_BLINDNESS_THEME = "syntax_themes/default_color_blindness.xml";
    }
}
