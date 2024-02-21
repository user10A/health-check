package healthcheck.repo.Dao;
import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.GlobalSearch.SearchResponse;
import java.util.List;

public interface DoctorDao {
    List<DoctorResponseByWord> getAllDoctorsBySearch(String word);
    List<DoctorResponseByWord> getAllDoctors();
    List<SearchResponse> globalSearch(String word);
}
