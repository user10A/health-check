package healthcheck.repo.Dao;

import healthcheck.dto.User.ResultUsersResponse;

import java.util.List;

public interface UserDao {
    List<ResultUsersResponse> getAllPatients();
}
