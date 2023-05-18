package xliic.openapi.example;

import java.util.List;
import java.util.Map;

public class UserService {
    boolean login (String user, String password) {return true;}
    User updateUser (User newUser) {return null;}

    public User getUser(String id) {
        return new User("foo@bar.com", "Foo Bar", "NotGivingYouMyPassword", "bronze", new String [] {"Account1","Account2"} );
    }
}
