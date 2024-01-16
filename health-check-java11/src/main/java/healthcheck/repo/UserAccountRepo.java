package healthcheck.repo;
import healthcheck.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepo extends JpaRepository<UserAccount,Long> {
    Optional<UserAccount> getUserByEmail(String email);
    UserAccountRepo findUserAccountByEmail(String email);
}