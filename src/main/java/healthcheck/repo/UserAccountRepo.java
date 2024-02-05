package healthcheck.repo;
import healthcheck.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount,Long> {
    Optional<UserAccount> getUserAccountByEmail(String email);
    boolean existsUserAccountByEmail(String email);
    UserAccount findUserAccountByEmail(String email);
    @Query("SELECT u FROM UserAccount u WHERE u.tokenPassword = :tokenPassword")
    Optional<UserAccount> getByUserAccountByTokenPassword(@Param("tokenPassword") String tokenPassword);
}