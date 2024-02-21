package healthcheck.api;

import healthcheck.dto.GlobalSearch.SearchResponse;
import healthcheck.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@CrossOrigin
@Tag(name = "GlobalSearch", description = "Expert Search API Endpoints")
public class GlobalSearchAPI {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "Global Search Method",
            description = "You can find a doctor by first name and last name, " +
                    "as well as discover a department.")
    public List<SearchResponse> globalSearch(@RequestParam(required = false) String word) {
        return searchService.search(word);
    }
}