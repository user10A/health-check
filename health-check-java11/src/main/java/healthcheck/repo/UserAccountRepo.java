package healthcheck.repo;
import healthcheck.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount,Long> {
    Optional<UserAccount> getUserAccountByEmail(String email);
    boolean existsUserAccountByEmail(String email);
}