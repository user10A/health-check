package healthcheck.service;

import healthcheck.dto.GlobalSearch.SearchResponse;

import java.util.List;

public interface SearchService {
    List<SearchResponse> search(String word);
}
