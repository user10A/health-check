package healthcheck.repo.Dao;
import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.Doctor.ResponseToGetDoctorsByDepartment;
import healthcheck.dto.GlobalSearch.SearchResponse;
import healthcheck.entities.Department;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorDao {
    List<ResponseToGetDoctorsByDepartment> getDoctorsByDepartment();
    List<DoctorResponseByWord> getAllDoctorsBySearch(String word);
    List<DoctorResponseByWord> getAllDoctors();
    List<SearchResponse> globalSearch(String word);
    Department getDepartmentByDoctorId(Long id);
}
