package healthcheck.service.Impl;
import healthcheck.S3.S3Service;
import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Department;
import healthcheck.entities.Result;
import healthcheck.entities.User;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.ResultRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class ResultServiceImpl implements ResultService {
    private final ResultRepo resultRepo;
    private final DepartmentRepo departmentRepo;
    private final UserRepo userRepo;
    private final S3Service s3Service;

    @Override
    public SimpleResponse saveResult(RequestSaveResult request) {
        try {
            Department department = departmentRepo.findByFacility(request.getFacility());

            User user = userRepo.findById(request.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Пользователь с ID: %d не найден", request.getUserId())
                    ));
            Result result = Result.builder()
                    .resultDate(request.getDataOfDelivery())
                    .pdfUrl(request.getUrl())
                    .department(department)
                    .resultNumber(generateTenDigitNumber())
                    .user(user)
                    .build();

            resultRepo.save(result);
            String successMessage = "Успешно сохранен!";
            log.info(successMessage);
            return new SimpleResponse(HttpStatus.OK, successMessage);
        } catch (Exception e) {
            String errorMessage = "Ошибка при сохранении заявки: " + e.getMessage();
            log.error(errorMessage, e);
            return SimpleResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("Произошла ошибка.").build();
        }
    }

    @Override
    public String getResultByResultNumberResult(Long resultNumber) {
        return resultRepo.getResultByResultNumberResult(resultNumber);
    }
    private static Long generateTenDigitNumber() {
        long randomNumber = (long) (Math.random() * 9000000000L) + 1000000000L;
        return randomNumber;
    }


}