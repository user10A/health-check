package healthcheck.repo;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByUserAccountEmail(String email);


}