package healthcheck.service.Impl;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ProfileResponse;
import healthcheck.dto.User.UserResponse;
import healthcheck.dto.User.UserResponseGetById;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.UserDao;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserAccountRepo userAccountRepo;
    private final UserDao userDao;

    @Override
    public ProfileResponse editUserProfile(ProfileRequest profileRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String email = authentication.getName();
            log.info(email);

            UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);

            userAccount.setEmail(profileRequest.getEmail());

            User user = userAccount.getUser();
            user.setFirstName(profileRequest.getFirstName());
            user.setLastName(profileRequest.getLastName());
            user.setPhoneNumber(profileRequest.getNumberPhone());

            userRepo.save(user);
            userAccountRepo.save(userAccount);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userAccount.getEmail(),
                            null,
                            userAccount.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            return ProfileResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(userAccount.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        } catch (Exception e) {
            log.error("Error editing user profile", e);
            throw new RuntimeException("Error editing user profile", e);
        }
    }

    @Override
    public List<UserResponse> getAllAppointmentsOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount user = userAccountRepo.getUserAccountByEmail(email).orElseThrow(() -> {
            log.error(String.format("User with email is not found !!!",email));
            return new NotFoundException("User is not found !!!");
        });
        return userDao.getAllAppointmentsOfUser(user.getId());
    }

    @Override
    public UserResponseGetById getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public int clearMyAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount user = userAccountRepo.getUserAccountByEmail(email).orElseThrow(() -> {
            log.error(String.format("User with email is not found !!!",email));
            return new NotFoundException("User is not found !!!");
        });
        return userDao.clearMyAppointments(user.getId());
    }
}