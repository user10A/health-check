package healthcheck.repo.Dao;
import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.Doctor.DoctorsGetAllByDepartmentsResponse;
import healthcheck.dto.Doctor.DoctorsGetAllByDepartmentsResponse1;
import healthcheck.dto.GlobalSearch.SearchResponse;
import java.util.List;

public interface DoctorDao {
    List<DoctorResponseByWord> getAllDoctorsBySearch(String word);
    List<DoctorResponseByWord> getAllDoctors();
    List<SearchResponse> globalSearch(String word);
    List<DoctorsGetAllByDepartmentsResponse1>getAllDoctorByDepartments (String facility);
    List<DoctorsGetAllByDepartmentsResponse> getAllDoctorsSortByDepartments();
}

