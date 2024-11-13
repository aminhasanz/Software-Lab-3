import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserService {
    private static final UserRepository repository = null;

    public static boolean loginWithUsername(String username, String password) {
        User userByUsername = repository.getUserByUsername(username);
        if (userByUsername == null) {
            return false;
        }
        return userByUsername.getPassword().equals(password);
    }

    public static boolean loginWithEmail(String email, String password) {
        User userByEmail = repository.getUserByEmail(email);
        if (userByEmail == null) {
            return false;
        }
        return userByEmail.getPassword().equals(password);
    }

    public boolean registerUser(String username, String password) {
        User user = new User(username, password);
        return repository.addUser(user);
    }

    public static boolean registerUser(String username, String password, String email) {
        User user = new User(username, password);
        user.setEmail(email);
        return repository.addUser(user);
    }

    public static boolean removeUser(String username) {
        return repository.removeUser(username);
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(repository.usersByUserName.values());
    }

    public static boolean changeUserEmail(String username, String newEmail) {
        if (!isValidEmail(newEmail)) {
            return false;
        }

        User user = repository.getUserByUsername(username);
        if (user != null) {
            if (user.getEmail() != null) {
                repository.usersByEmail.remove(user.getEmail());
            }
            user.setEmail(newEmail);
            repository.usersByEmail.put(newEmail, user);
            return true;
        }
        return false;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}
