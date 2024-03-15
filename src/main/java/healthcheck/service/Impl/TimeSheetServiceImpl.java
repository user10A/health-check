package healthcheck.service.Impl;

import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.repo.Dao.TimeSheetDao;
import healthcheck.service.TimeSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSheetServiceImpl implements TimeSheetService {

    private  final TimeSheetDao timeSheetDao;
    @Override
    public List<TimeSheetResponse> getTimesheetDoctor(@RequestParam String facility) {
        return timeSheetDao.getTimesheetDoctor(facility);
    }
    @Override
    public List<TimeSheetResponse> getTimesheetDoctorById(Long id) {
        return timeSheetDao.getTimesheetDoctorById(id);
    }
}