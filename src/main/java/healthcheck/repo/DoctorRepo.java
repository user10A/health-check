package healthcheck.repo;

import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    List<Doctor> getDoctorsByDepartment(Department department);

    @Query("""
    SELECT new healthcheck.dto.Doctor.DoctorResponseByWord (
        d.id,
        d.image,
        d.isActive,
        d.firstName,
        d.lastName,
        d.department,
        d.schedule.endDateWork
    )
    FROM Doctor d
    WHERE concat(d.firstName, ' ', d.lastName) LIKE %:word%
    ORDER BY d.firstName, d.lastName
    """)
    List<DoctorResponseByWord> getAllDoctorsBySearch(@Param("word") String word);

    @Query("""
    SELECT new healthcheck.dto.Doctor.DoctorResponseByWord (
        d.id,
        d.image,
        d.isActive,
        d.firstName,
        d.lastName,
        d.department,
        d.schedule.endDateWork
    )
    FROM Doctor d
    ORDER BY d.firstName, d.lastName
    """)
    List<DoctorResponseByWord> getAllDoctors();
}