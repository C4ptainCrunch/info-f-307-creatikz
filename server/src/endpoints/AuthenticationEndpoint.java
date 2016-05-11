package endpoints;

import constants.Network;
import database.DAOFactory;
import database.UsersDAO;
import models.users.User;
import utils.TokenCreator;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/authentication")
public class AuthenticationEndpoint {
    UsersDAO usersDAO = DAOFactory.getInstance().getUsersDAO();
    final static Map<String, String> tokens = new HashMap<>();

    @POST
    @Produces("text/plain")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {

        try {
            authenticate(username, password);
            String token = issueToken(username);
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void authenticate(String username, String password) throws Exception {
        User testUser = this.usersDAO.findByUsernameAndPassword(username, password);
        if (testUser == null) {
            throw new Exception("No user");
        }
        if (!this.usersDAO.isActivated(testUser)) {
            throw new Exception("Not activated");
        }
    }

    private String issueToken(String username) {
        String token = TokenCreator.newToken();
        this.tokens.put(token, username);
        return token;
    }

    public static boolean validateToken(String token) {
        return tokens.containsKey(token);
    }
}
