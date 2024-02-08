package healthcheck.service.Impl;

import healthcheck.dto.GlobalSearch.SearchResponse;
import healthcheck.entities.Doctor;
import healthcheck.repo.DoctorRepo;
import healthcheck.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final DoctorRepo doctorRepo;
    private static final Logger logger = Logger.getLogger(Doctor.class.getName());

    @Override
    public List<SearchResponse> search(String word) {
        logger.info("Поиск термина: {}" + word);
        List<SearchResponse> results = doctorRepo.globalSearch(word);
        logger.info("Найдено {} результатов для термина: {}" + results.size() + word);
        return results;
    }
}