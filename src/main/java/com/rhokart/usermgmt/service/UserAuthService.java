package com.rhokart.usermgmt.service;

import com.rhokart.usermgmt.document.*;
import com.rhokart.usermgmt.dto.AccessToken;
import com.rhokart.usermgmt.dto.AuthenticatedUser;
import com.rhokart.usermgmt.dto.LoginInfo;
import com.rhokart.usermgmt.dto.RefreshTokenInfo;
import com.rhokart.usermgmt.dto.request.Person;
import com.rhokart.usermgmt.exceptation.IncomingPayloadException;
import com.rhokart.usermgmt.exceptation.InvalidAppContext;
import com.rhokart.usermgmt.exceptation.UserNotFound;
import com.rhokart.usermgmt.model.EmailDetails;
import com.rhokart.usermgmt.repository.AppVersionRepo;
import com.rhokart.usermgmt.repository.OtpDocRepo;
import com.rhokart.usermgmt.repository.RefreshTokenRepo;
import com.rhokart.usermgmt.repository.UserRepository;
import com.rhokart.usermgmt.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.rhokart.usermgmt.util.GlobalConstants.*;

@Service
@Slf4j
public class UserAuthService {
    @Autowired
    private IEmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpMongoDbImpl iOtpUtils;
    @Autowired
    private OtpDocRepo otpProcessRepository;
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private AppVersionRepo appVersionRepo;

    @Autowired
    private JwtHelper jwtHelper;

    public OtpResponse getOtpByMobile(String mobileNo, String applicationId) {
        if (!Utils.validateApplicationContext(applicationId)) {
            throw new InvalidAppContext("Invalid Application Context",
                    "Please Check AppId or AppName.");
        }

        if (mobileNo.trim().length() != MOB_LENGTH)
            throw new IncomingPayloadException("Bad Request",
                    "Please enter your 10 digit phone number");

        OtpResponse otpResponse = new OtpResponse();
        OtpDoc gotp = new OtpDoc(mobileNo, iOtpUtils.generateOtp(mobileNo, OTP_LENGTH),
                OTP_VALIDITY, System.currentTimeMillis());
        OtpDoc rotp = otpProcessRepository.save(gotp);

        // Send to send email using OTP Services.
        // TODO...

        otpResponse.setMobileNo(rotp.getUserKey());
        otpResponse.setValidityInSecond(rotp.getValidityInSecond());
        otpResponse.setGenerationTimeStamp(rotp.getGenerationTimeStamp());
        otpResponse.setEmail(null);
        return otpResponse;
    }

    public OtpResponse getOtpByEmail(String email, String applicationId) {
        if (!Utils.validateApplicationContext(applicationId)) {
            throw new InvalidAppContext("Invalid Application Context",
                    "Please Check AppId or AppName.");
        }
        OtpDoc gotp = new OtpDoc(email, iOtpUtils.generateOtp(email, OTP_LENGTH), OTP_VALIDITY, System.currentTimeMillis());
        OtpDoc rotp = otpProcessRepository.save(gotp);

        EmailDetails details = EmailDetails.builder()
                .msgBody("Login OTP for RHOKART is "+gotp.getOtp()+"\n\n\n\nRegards,\nRhoselect Private Limited")
                .subject("RHOKART Login OTP")
                .toAddress(email.trim())
                .build();

        if(!emailService.sendEmailToUser(details)) {
            log.error("Email send fail..");
            throw new UserNotFound("User email send fail","Not able to send email to user");
        }
        OtpResponse otpResponse = new OtpResponse();
        otpResponse.setEmail(rotp.getUserKey());
        otpResponse.setValidityInSecond(rotp.getValidityInSecond());
        otpResponse.setGenerationTimeStamp(rotp.getGenerationTimeStamp());
        otpResponse.setMobileNo(null);
        return otpResponse;
    }

    public AuthenticatedUser userLogin(LoginInfo loginInfo) {
        if (!Utils.validateApplicationContext(loginInfo.getApplicationContext().getApplicationId(),
                loginInfo.getApplicationContext().getApplicationName())) {
            return AuthenticatedUser.builder()
                    .build();
        }
        if (!iOtpUtils.validateOtp(loginInfo.getEmail(), loginInfo.getOtp())) {
            log.info("Incorrect OTP..please send correct OTP :{}", loginInfo);
            return AuthenticatedUser.builder()
                    .build();
        }
        String userId;
        String defaultPincode = "";
        List<Role> roles;
        boolean isProfileCompleted = Boolean.FALSE;
        boolean isNewUser = Boolean.TRUE;

        if (loginInfo.getEmail() != null && !loginInfo.getEmail().isEmpty()) {
            Optional<User> user = userRepository.findByEmail(loginInfo.getEmail());
            if (user.isPresent()) {
                userId = user.get().getUserId();
                defaultPincode = user.get().getDefaultPincode();
                roles = user.get().getRoles();
                isNewUser = Boolean.FALSE;
                user.get().setUpdatedAt((int) Instant.now().getEpochSecond());
                userRepository.save(user.get());

                if(user.get().isProfileCompleted())
                    isProfileCompleted = Boolean.TRUE;

            } else {
                userId = Utils.generateUserId();
                roles = List.of(Role.builder().roleId(BUYER_ROLEID).name(BUYER_USER).build());
                User newUser = User.builder()
                        .userId(userId)
                        .email(loginInfo.getEmail())
                        .roles(roles)
                        .defaultPincode(defaultPincode)
                        .isProfileCompleted(Boolean.FALSE)
                        .build();

                newUser.setUpdatedAt((int) Instant.now().getEpochSecond());   // setting for verification of user for device per login.
                userRepository.save(newUser);
            }
        } else {
            log.info("Invalid payload in corporate login. CorporateLogin ::{}", loginInfo);
            return AuthenticatedUser.builder()
                    .build();
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(userId);
        refreshToken.setOwner(userId);
        refreshToken.setCreatedBy(userId);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshTokenRepo.save(refreshToken);
        Date loginTime = new Date();
        String accessToken = jwtHelper.generateAccessToken(userId, loginTime);
        String refreshTokenString = jwtHelper.generateRefreshToken(userId, refreshToken.getId());


        // *******Getting application version here. **************************//
        List<AppVersion> appVersions = appVersionRepo.findAll();
        AtomicReference<String> androidVersion = new AtomicReference<>();
        AtomicReference<String> iOSVersion = new AtomicReference<>();
        appVersions.forEach( app -> {
            if(app.getIsActive()){
                if(app.getAppId().equalsIgnoreCase(APP_ID_ANDRIOD) && app.getAppName().equalsIgnoreCase(APP_NAME_RHOKART))
                    androidVersion.set(app.getAppVersion());
                if(app.getAppId().equalsIgnoreCase(APP_ID_IOS) && app.getAppName().equalsIgnoreCase(APP_NAME_RHOKART))
                    iOSVersion.set(app.getAppVersion());

            }
        });
        //**************** VERSION CONTROL END HERE *************************//


        return AuthenticatedUser.builder()
                .userId(userId)
                .email(loginInfo.getEmail())
                .mobileNo(loginInfo.getMobileNo())
                .refreshToken(refreshTokenString)
                .accessToken(accessToken)
                .isProfileCompleted(isProfileCompleted)
                .roles(roles)
                .isNewUser(isNewUser)
                .defaultPincode(defaultPincode)
                .iOSAppVersion(iOSVersion.get())
                .androidAppVersion(androidVersion.get())
                .build();
    }

    public AuthenticatedUser oAuthUserLogin(Person person){
        User oauthUser = new User();
        boolean isNewUser = Boolean.TRUE;
        boolean isProfileCompleted = Boolean.FALSE;

        if(!person.getEmail().isEmpty()){
            log.info("oauth1 login for email: {}",person.getEmail());
            Optional<User> user = userRepository.findByEmail(person.getEmail());
            if (user.isPresent()) {
                log.info("oauth User already present...");
                oauthUser.setUserId(user.get().getUserId());
                oauthUser.setEmail(user.get().getEmail());
                oauthUser.setRoles(user.get().getRoles());
                oauthUser.setPrimaryMob(user.get().getPrimaryMob());
                oauthUser.setFirstName(user.get().getFirstName());
                oauthUser.setLastName(user.get().getLastName());
                oauthUser.setDefaultPincode(user.get().getDefaultPincode());
                user.get().setUpdatedAt((int)Instant.now().getEpochSecond());

                userRepository.save(user.get());

                isNewUser = Boolean.FALSE;
                if(user.get().isProfileCompleted())
                    isProfileCompleted = Boolean.TRUE;

            } else {
                log.info("oauth new user is created on Systems");
                oauthUser.setUserId( Utils.generateUserId());
                oauthUser.setRoles(List.of(Role.builder().roleId(BUYER_ROLEID).name(BUYER_USER).build()));
                oauthUser.setEmail(person.getEmail());
                oauthUser.setProfileCompleted(Boolean.FALSE);
                oauthUser.setFirstName(person.getGivenName());
                oauthUser.setLastName(person.getFamilyName());
                oauthUser.setPrimaryMob(person.getPhoneNumber());
                oauthUser.setProfilePictureUri(person.getProfilePictureUri());
                oauthUser.setUpdatedAt((int)Instant.now().getEpochSecond());
                oauthUser.setDefaultPincode("");

                userRepository.save(oauthUser);
            }
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(oauthUser.getUserId());
        refreshToken.setOwner(oauthUser.getUserId());
        refreshToken.setCreatedBy(oauthUser.getUserId());
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshTokenRepo.save(refreshToken);
        Date loginTime = new Date();
        String accessToken = jwtHelper.generateAccessToken(oauthUser.getUserId(), loginTime);
        String refreshTokenString = jwtHelper.generateRefreshToken(oauthUser.getUserId(), refreshToken.getId());

        // *******Getting application version here. **************************//
        List<AppVersion> appVersions = appVersionRepo.findAll();
        AtomicReference<String> androidVersion = new AtomicReference<>();
        AtomicReference<String> iOSVersion = new AtomicReference<>();
         appVersions.forEach( app -> {
            if(app.getIsActive()){
                if(app.getAppId().equalsIgnoreCase(APP_ID_ANDRIOD) && app.getAppName().equalsIgnoreCase(APP_NAME_RHOKART))
                    androidVersion.set(app.getAppVersion());
                if(app.getAppId().equalsIgnoreCase(APP_ID_IOS) && app.getAppName().equalsIgnoreCase(APP_NAME_RHOKART))
                    iOSVersion.set(app.getAppVersion());

            }
        });
        //**************** VERSION CONTROL END HERE *************************//

        return AuthenticatedUser.builder()
                .userId(oauthUser.getUserId())
                .email(oauthUser.getEmail())
                .mobileNo(oauthUser.getPrimaryMob())
                .refreshToken(refreshTokenString)
                .accessToken(accessToken)
                .isProfileCompleted(isProfileCompleted)
                .roles(oauthUser.getRoles())
                .isNewUser(isNewUser)
                .defaultPincode(oauthUser.getDefaultPincode())
                .androidAppVersion(androidVersion.get())
                .iOSAppVersion(iOSVersion.get())
                .build();

    }

    public AccessToken refreshAccessToken(RefreshTokenInfo refreshToken){
        User user = userRepository.findById(refreshToken.getUserId()).orElse(null);
        if(Objects.isNull(user)){
            return AccessToken.builder()
                    .userId(refreshToken.getUserId())
                    .accessToken("")
                    .message(Invalid_User_ID)
                    .returnCode(HttpStatus.UNAUTHORIZED.toString())
                    .build();
        }
        if (jwtHelper.validateRefreshToken(refreshToken.getRefreshToken()) && refreshTokenRepo.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshToken.getRefreshToken()))) {
            RefreshToken storedToken =  refreshTokenRepo.findById(jwtHelper.getTokenIdFromRefreshToken(refreshToken.getRefreshToken())).orElse(null);

            if(Objects.isNull(storedToken)){
                return AccessToken.builder()
                        .userId(refreshToken.getUserId())
                        .accessToken("")
                        .returnCode(HttpStatus.UNAUTHORIZED.toString())
                        .message(Invalid_Refresh_Token)
                        .build();
            }

            user.setUpdatedAt((int) Instant.now().getEpochSecond());
            userRepository.save(user);

            return AccessToken.builder()
                    .userId(refreshToken.getUserId())
                    .accessToken(jwtHelper.generateAccessToken(storedToken.getOwner(),new Date()))
                    .returnCode(SUCCESS_CODE)
                    .message(SUCCESSFULLY_TOKEN_GENERATED)
                    .build();
        }
        log.error("refresh token is invalid.. please try with different token. {}", refreshToken);
        return AccessToken.builder()
                .userId(refreshToken.getUserId())
                .message(Invalid_Refresh_Token)
                .returnCode(HttpStatus.UNAUTHORIZED.toString())
                .build();
    }
}
