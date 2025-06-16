import com.messagerie.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    void deleteUser(Long Id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}