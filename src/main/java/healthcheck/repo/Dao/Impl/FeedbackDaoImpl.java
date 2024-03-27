package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Feedback.FeedbackDaoResponse;
import healthcheck.dto.Feedback.FeedbackResponse;
import healthcheck.dto.Feedback.RatingResponse;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.FeedbackDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedbackDaoImpl implements FeedbackDao {

    private final JdbcTemplate jdbcTemplate;
    private final MessageSource messageSource;

    @Override
    public FeedbackResponse getFeedbackByDoctorId(Long id) {
        try {
            var sql = """
            SELECT
                d.id,
                d.image,
                CONCAT(d.first_name, ' ', d.last_name) AS doctor_full_name,
                d2.facility,
                avg(f.rating) as rating,
                count(f) as feedback_count
            FROM
                Doctor d
            JOIN
                department d2 ON d.department_id = d2.id
            JOIN
                feedback f ON f.doctor_id = d.id
            WHERE d.id =? 
            GROUP BY
                d.id,
                d.image,
                doctor_full_name,
                d2.facility;
            """;
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> FeedbackResponse.builder()
                    .doctorId(rs.getLong(1))
                    .image(rs.getString(2))
                    .fullName(rs.getString(3))
                    .department(rs.getString(4))
                    .averageRating(rs.getDouble(5))
                    .count(rs.getInt(6))
                    .feedbacks(getFeedback(rs.getLong(1)))
                    .build());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public RatingResponse getRaringResponseByIdDoctor(Long id) {
        try {
            var sql = """
            SELECT
            avg(f.rating) as rating,
            count(f) as count_rating
            FROM
            doctor d
            JOIN
            feedback f ON f.doctor_id = d.id
            WHERE d.id = ?
            """;
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    RatingResponse.builder()
                    .averageRating(rs.getDouble(1))
                    .countRating(rs.getInt(2))
                    .build());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
    }

    public List<FeedbackDaoResponse> getFeedback(Long id){
        try {
            var sql = """
            SELECT
             f.id,
             concat(u.first_name,' ',u.last_name ) as full_name,
             f.local_date as local_date,
             f.rating,
             f.comment,
             f.creation_date as creation_date
            FROM
            Doctor d
            JOIN
            feedback f ON f.doctor_id = d.id
            JOIN
            users u ON f.user_id = u.id
            WHERE d.id = ?
            ORDER BY creation_date;
            """;
            return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> FeedbackDaoResponse.builder()
                    .feedbackId(rs.getLong(1))
                    .userFullName(rs.getString(2))
                    .localDate(rs.getDate(3).toLocalDate())
                    .rating(rs.getInt(4))
                    .comment(rs.getString(5))
                    .build());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
    }

}

