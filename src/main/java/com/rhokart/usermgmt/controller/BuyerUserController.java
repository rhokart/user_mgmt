package com.rhokart.usermgmt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhokart.datamodel.dto.*;
import com.rhokart.usermgmt.dto.*;
import com.rhokart.usermgmt.dto.request.LogoutInfo;
import com.rhokart.usermgmt.dto.request.Person;
import com.rhokart.usermgmt.exceptation.InvalidToken;
import com.rhokart.usermgmt.repository.RefreshTokenRepo;
import com.rhokart.usermgmt.service.JwtHelper;
import com.rhokart.usermgmt.service.UserAuthService;
import com.rhokart.usermgmt.service.UserDetailService;
import com.rhokart.datamodel.util.Constants;
import com.rhokart.usermgmt.util.GlobalConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;
import static com.rhokart.usermgmt.util.GlobalConstants.INVALID_TOKEN;

@RestController
@RequestMapping("/userauth")
@Slf4j
public class BuyerUserController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UserDetailService userDetailService;

    @PostMapping("/otp")
    @SneakyThrows
    public ResponseEntity<Response> getOtp(@RequestBody @Valid UserKeyInfo userKeyInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(Response.builder()
                    .failure(FailureInfo.builder().errMessage(errorMessage).errStatus(16).build())
                            .status(Constants.STATUS_FAIL)
                    .build());
        }
        log.debug("Buyer user get otp request :: {}",objectMapper.writeValueAsString(userKeyInfo));
        ResponseData otpResponse = userKeyInfo.getEmail() != null && !userKeyInfo.getEmail().isEmpty() ?
                userAuthService.getOtpByEmail(userKeyInfo.getEmail(), userKeyInfo.getApplicationId())
                :userAuthService.getOtpByMobile(userKeyInfo.getMobile(), userKeyInfo.getApplicationId());

        Response response = Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .coreData(CoreData.builder().responseData(otpResponse).build())
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .successCode(Constants.SUCCESS_CODE)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @SneakyThrows
    public ResponseEntity<Response> login(@Valid @RequestBody LoginInfo loginInfo){
        AuthenticatedUser authenticatedUser = userAuthService.userLogin(loginInfo);

        if(Objects.isNull(authenticatedUser.getUserId()) || authenticatedUser.getUserId().isEmpty()){
            Response response = Response.builder()
                    .status(Constants.STATUS_FAIL)
                    .failure(FailureInfo.builder()
                            .errCode(8)
                            .errMessage("Not able to login")
                            .errStatus(415)
                            .errDescription("OTP/User is not valid")
                            .build())
                    .build();

            return ResponseEntity.ok(response);
        }

        Response response = Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .successCode(Constants.SUCCESS_CODE)
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .coreData(CoreData.builder().responseData(authenticatedUser).build())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    @SneakyThrows
    public ResponseEntity<Response> userRegistrationProcess(@RequestBody RegistrationInfo registrationInfo){
        ResponseData updatedUser = userDetailService.doUserRegistrationUpdate(registrationInfo);

        if(Objects.isNull(updatedUser)){
            Response response = Response.builder()
                    .status(Constants.STATUS_FAIL)
                    .failure(FailureInfo.builder()
                            .errCode(8)
                            .errMessage("UserId not found")
                            .errStatus(415)
                            .errDescription("Correct userId and send again...")
                            .build())
                    .build();

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .successCode(Constants.SUCCESS_CODE)
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .coreData(CoreData.builder().responseData(updatedUser).build())
                                .build())

                .build());
    }


    @PutMapping("/refresh")
    @SneakyThrows
    public ResponseEntity<Response> refreshAccessToken(@Valid @RequestBody RefreshTokenInfo refreshAccessToken) {

        return ResponseEntity.ok(
                Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .coreData(CoreData.builder().responseData(userAuthService.refreshAccessToken(refreshAccessToken)).build())
                                .successCode(Constants.SUCCESS_CODE)
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .build())
                        .build());
    }


    @PutMapping("/logout")
    @SneakyThrows
    public ResponseEntity<Response> logout(@Valid @RequestBody LogoutInfo logoutInfo) {
        String refreshTokenString = logoutInfo.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepo.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            refreshTokenRepo.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            return ResponseEntity.ok(Response.builder()
                            .status(Constants.STATUS_SUCCESS)
                            .success(SuccessInfo.builder()
                                    .execTimestamp(Constants.getCurrentTimestamp())
                                    .successCode(Constants.SUCCESS_CODE)
                                    .coreData(CoreData.builder().responseData(Logout.builder()
                                            .isLogoutSuccessful(Boolean.TRUE)
                                            .logoutMessage(GlobalConstants.SUCCESSFULLY_LOGOUT).build())
                                            .build())
                                    .build())
                    .build());
        }
        throw new InvalidToken(INVALID_TOKEN, "Refresh Token is Expired or Invalid");
    }

    @PostMapping("/oauth/login")
    @SneakyThrows
    public ResponseEntity<Response> oauthLogin(@Valid @RequestBody Person person){

        if(person.getEmail().isEmpty() || Objects.isNull(person.getEmail()))
            return ResponseEntity.ok(Response.builder()
                            .failure(FailureInfo.builder().errCode(16)
                                    .errDescription("Missing email .. pls provide email!!!")
                                    .errMessage("email is missing")
                                    .errStatus(1600)
                                    .build())
                            .status(Constants.STATUS_FAIL)
                            .success(SuccessInfo.builder()
                                    .execTimestamp(Constants.getCurrentTimestamp())
                                    .successCode(Constants.FAILED_CODE)
                                    .coreData(CoreData.builder()
                                            .responseData(null)
                                            .build())
                                    .build())
                    .build());


        ResponseData oauthUserLogin = userAuthService.oAuthUserLogin(person);

        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .successCode(Constants.SUCCESS_CODE)
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .coreData(CoreData.builder().responseData(oauthUserLogin).build())
                        .build())
                .build());
    }


    @GetMapping("/userdetail")
    public ResponseEntity<Response> getUserDetails(@Valid @RequestParam("userId") String userId){
        UserDetail detail = userDetailService.getUserDetailByUserId(userId);

        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .successCode(Constants.SUCCESS_CODE)
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .coreData(CoreData.builder().responseData(detail).build())
                        .build())
                .build());

    }
}
