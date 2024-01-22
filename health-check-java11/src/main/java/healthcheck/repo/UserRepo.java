package healthcheck.repo;
import healthcheck.dto.User.ResultUsersResponse;
import healthcheck.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByUserAccountEmail(String email);
    @Query("""
    SELECT new healthcheck.dto.User.ResultUsersResponse (
       u.id,
        concat(u.firstName, ' ', u.lastName),
        u.phoneNumber,
        u.userAccount.email,
        r.resultDate)
    FROM User u
    JOIN Result r ON r.id = u.id
    WHERE concat(u.firstName, ' ', u.lastName) LIKE %:word%
    OR u.userAccount.email LIKE %:word%
    ORDER BY u.firstName ,u.lastName
    """)
    List<ResultUsersResponse> resultUsersBySearch(@Param("word") String word);

}