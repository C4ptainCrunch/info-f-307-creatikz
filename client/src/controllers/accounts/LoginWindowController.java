package controllers.accounts;

import constants.Errors;
import constants.Network;
import models.NetworkRequest;
import views.accounts.LoginWindowView;
import views.accounts.SignUpView;
import views.accounts.TokenActivationView;
import views.management.ProjectManagementView;

import javax.swing.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The controller related to the LoginWindowView.
 */

public class LoginWindowController {

    private final LoginWindowView view;
    private final String BASE_PATH = "user/login/";

    public LoginWindowController(LoginWindowView view) {
        this.view = view;
    }

    /**
     * Launch the login process.
     * @param username The user's username
     * @param password The user's password
     */

    public void login(String username, String password) {
        Form postForm = new Form();
        postForm.param("username", username);
        postForm.param("password", password);

        NetworkRequest request = new NetworkRequest(Network.HOST.COMPLETE_HOSTNAME,BASE_PATH+username, MediaType.TEXT_PLAIN_TYPE);
        request.post(postForm);

        String response = request.getResponseAsString();

        if(response.equals(Network.Login.LOGIN_OK)){
            this.view.dispose();
            new ProjectManagementView();
        }else if(response.equals(Network.Login.ACCOUNT_NOT_ACTIVATED)){
            JOptionPane.showMessageDialog(this.view, Errors.ACTIVE_ACCOUNT_FIRST, Errors.ERROR, JOptionPane.ERROR_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(this.view, Errors.LOGIN_FAILED, Errors.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Launch the sign up process.
     */

    public void signUp() {
        new SignUpView(this.view);
    }

    /**
     * Launch the token activation process.
     */

    public void tokenActivation() {
        new TokenActivationView();
    }
}