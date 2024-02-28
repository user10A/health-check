package healthcheck.doctorTest.doctorTest;

import healthcheck.HealthCheckJava11Application;
import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Department;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.DoctorRepo;
import healthcheck.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = HealthCheckJava11Application.class)
@Slf4j
public class DoctorServiceTest {

    private final DoctorService doctorService;
    private final DepartmentRepo departmentRepo;
    private final DoctorRepo doctorRepo;

    @Autowired
    public DoctorServiceTest(DoctorService doctorService, DepartmentRepo departmentRepo, DoctorRepo doctorRepo) {
        this.doctorService = doctorService;
        this.departmentRepo = departmentRepo;
        this.doctorRepo = doctorRepo;
    }

    @Test
    @DisplayName("Сохранение врача с существующим отделением")
    public void saveDoctor_ExistingDepartment_Success() {
        DoctorSaveRequest request = DoctorSaveRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .position("Surgeon")
                .image("https://example.com/doctor.jpg")
                .description("Highly skilled surgeon specializing in cardiovascular procedures.")
                .build();

        try {
            Long departmentId = 2L;
            Department existingDepartment = new Department();
            existingDepartment.setId(departmentId);

            request.setDepartmentId(departmentId);

            SimpleResponse response = doctorService.saveDoctor(request);

            log.info("Результат сохранения врача: HttpStatus - {}, Message - {}", response.getHttpStatus(), response.getMessage());

            assertEquals(HttpStatus.OK, response.getHttpStatus());
            assertEquals("Успешно сохранен!", response.getMessage());

        } catch (Exception e) {
            log.error("Ошибка при сохранении врача: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Сохранение врача с несуществующим отделением")
    public void saveDoctor_NonExistingDepartment_ThrowsException() {
        DoctorSaveRequest request = DoctorSaveRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .position("Pediatrician")
                .image("https://example.com/doctor2.jpg")
                .description("Experienced pediatrician with a focus on child health.")
                .build();

        try {
            Long nonExistingDepartmentId = 99L;
            request.setDepartmentId(nonExistingDepartmentId);

            SimpleResponse response = doctorService.saveDoctor(request);

            log.info("Результат сохранения врача с несуществующим отделением: HttpStatus - {}, Message - {}", response.getHttpStatus(), response.getMessage());

            assertEquals(String.format("Отделение с ID: %d не найдено", nonExistingDepartmentId), response.getMessage());

        } catch (Exception e) {
            log.error("Ошибка при сохранении врача с несуществующим отделением: " + e.getMessage());
            throw e;
        }
    }
}