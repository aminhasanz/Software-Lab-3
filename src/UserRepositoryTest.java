import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private UserRepository repository;

    @Before
    public void setUp() {
        List<User> userList = Arrays.asList(
                new User("admin", "1234"),
                new User("ali", "qwert"),
                new User("mohammad", "123asd"),
                new User("amin", "asdf", "amin@gmail.com"));
        repository = new UserRepository(userList);
    }

    @Test
    public void getContainingUser__ShouldReturn() {
        User ali = repository.getUserByUsername("ali");
        assertNotNull(ali);
        assertEquals("ali", ali.getUsername());
        assertEquals("qwert", ali.getPassword());
    }

    @Test
    public void getNotContainingUser__ShouldReturnNull() {
        User user = repository.getUserByUsername("reza");
        assertNull(user);
    }

    @Test
    public void getContainingUserByEmail__ShouldReturn() {
        User amin = repository.getUserByEmail("amin@gmail.com");
        assertNotNull(amin);
        assertEquals("amin@gmail.com", amin.getEmail());
        assertEquals("asdf", amin.getPassword());
    }

    @Test
    public void getNotContainingUserByEmail__ShouldReturnNull() {
        User user = repository.getUserByEmail("reza@gmail.com");
        assertNull(user);
    }

    @Test
    public void createRepositoryWithDuplicateUsers__ShouldThrowException() {
        User user1 = new User("ali", "1234");
        User user2 = new User("ali", "4567");
        assertThrows(IllegalArgumentException.class, () -> {
//            new UserRepository(List.of(user1, user2));
        });
    }

    @Test
    public void addNewUser__ShouldIncreaseUserCount() {
        int oldUserCount = repository.getUserCount();

        // Given
        String username = "reza";
        String password = "123abc";
        String email = "reza@sharif.edu";
        User newUser = new User(username, password);

        // When
        repository.addUser(newUser);

        // Then
        assertEquals(oldUserCount + 1, repository.getUserCount());
    }

    @Test
    public void removeUser__ShouldDecreaseUserCount() {
        int oldUserCount = repository.getUserCount();

        // Given
        String username = "ali";

        // When
        boolean removed = repository.removeUser(username);

        // Then
        assertTrue(removed);
        assertEquals(oldUserCount - 1, repository.getUserCount());
    }

    @Test
    public void changeUserEmail__ShouldAllowLoginWithNewEmail() {
        // Given
        String username = "amin";
        String newEmail = "newamin@gmail.com";

        // When
        boolean changed = UserService.changeUserEmail(username, newEmail);

        // Then
        assertTrue(changed);
        assertNull(repository.getUserByEmail("amin@gmail.com"));
        User user = repository.getUserByEmail(newEmail);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }
}
