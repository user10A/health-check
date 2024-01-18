package healthcheck.service.Impl;

import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ProfileResponse;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserAccountRepo userAccountRepo;

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
}