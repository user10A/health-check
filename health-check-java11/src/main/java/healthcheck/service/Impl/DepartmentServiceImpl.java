package healthcheck.service.Impl;
import healthcheck.dto.Department.DepartmentResponse;
import healthcheck.repo.Dao.DepartmentDao;
import healthcheck.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDao departmentDao;

    @Override
    public List<DepartmentResponse> getAllFacility() {
        return departmentDao.getAllFacility();
    }
}