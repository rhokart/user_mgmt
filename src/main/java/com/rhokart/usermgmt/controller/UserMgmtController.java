package com.rhokart.usermgmt.controller;

import com.rhokart.datamodel.dto.CoreData;
import com.rhokart.datamodel.dto.FailureInfo;
import com.rhokart.datamodel.dto.Response;
import com.rhokart.datamodel.dto.SuccessInfo;
import com.rhokart.datamodel.util.Constants;
import com.rhokart.usermgmt.document.DeletedUser;
import com.rhokart.usermgmt.document.User;
import com.rhokart.usermgmt.dto.DeleteResponse;
import com.rhokart.usermgmt.dto.request.UpdatePincodeRequest;
import com.rhokart.usermgmt.dto.request.RemoveRequest;
import com.rhokart.usermgmt.model.DeleteUserResponse;
import com.rhokart.usermgmt.model.UpdatePincodeResponse;
import com.rhokart.usermgmt.repository.DeletedUserRepository;
import com.rhokart.usermgmt.repository.UserRepository;
import com.rhokart.usermgmt.service.UserDetailService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserMgmtController {
    @Autowired
    private UserDetailService userDetailService;
    private final UserRepository userRepository;
    private final DeletedUserRepository deletedUserRepository;

    public UserMgmtController(UserRepository userRepository, DeletedUserRepository deletedUserRepository) {
        this.userRepository = userRepository;
        this.deletedUserRepository = deletedUserRepository;
    }

    @DeleteMapping("/delete")
    @SneakyThrows
    public ResponseEntity<Response> deleteUser(@RequestParam("userId") String userId) {
        log.info("User delete request :: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Convert User to DeletedUser
            DeletedUser deletedUser = DeletedUser.builder()
                    .userId(user.getUserId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .gender(user.getGender())
                    .email(user.getEmail())
                    .primaryMob(user.getPrimaryMob())
                    .secondaryMob(user.getSecondaryMob())
                    .userAddress(user.getUserAddress())
                    .businessDetail(user.getBusinessDetail())
                    .isProfileCompleted(user.isProfileCompleted())
                    .profilePictureUri(user.getProfilePictureUri())
                    .roles(user.getRoles())
                    .build();

            deletedUserRepository.save(deletedUser);
            userRepository.delete(user);

            return ResponseEntity.ok(Response.builder()
                    .status(Constants.STATUS_SUCCESS)
                    .success(SuccessInfo.builder()
                            .successCode(Constants.SUCCESS_CODE)
                            .execTimestamp(Constants.getCurrentTimestamp())
                            .coreData(CoreData.builder().responseData(DeleteUserResponse.builder()
                                            .userId(userId)
                                                    .message("Deleted user successfully")
                                                    .isDeleted(Boolean.TRUE)

                                    .build()).build())
                            .build())

                    .build());
        }
        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_FAIL)
                .failure(FailureInfo.builder()
                        .errCode(8)
                        .errStatus(8080)
                        .errMessage("Error:Not able to delete user")
                        .errMessage("User is not delete , please try again.!!!")
                        .build())
                .build());
    }

    @PostMapping("/pincode")
    public ResponseEntity<Response> updateDefaultPincode(@Valid @RequestBody UpdatePincodeRequest updatePincodeRequest){
        Optional<User> user = userRepository.findById(updatePincodeRequest.getUserId());
        if(user.isPresent()){
            user.get().setDefaultPincode(updatePincodeRequest.getPincode());
            userRepository.save(user.get());
            return ResponseEntity.ok(Response.builder()
                    .status(Constants.STATUS_SUCCESS)
                    .success(SuccessInfo.builder()
                            .successCode(Constants.SUCCESS_CODE)
                            .execTimestamp(Constants.getCurrentTimestamp())
                            .coreData(CoreData.builder().responseData(UpdatePincodeResponse.builder()
                                    .message("Pincode updated successfully")
                                    .pincode(updatePincodeRequest.getPincode())
                                    .userId(updatePincodeRequest.getUserId()).build()).build())
                            .build())
                    .build());
        }

        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_FAIL)
                .failure(FailureInfo.builder()
                        .errCode(8)
                        .errStatus(8080)
                        .errMessage("Error:Not able to update pincode")
                        .errMessage("Pincode is not updated , please try again.!!!")
                        .build())
                .build());

    }
    @CrossOrigin(origins = "*")
    @PostMapping("/web/account/delete")
    public DeleteResponse deleteUserFromWeb(@RequestBody RemoveRequest removeRequest){
        return DeleteResponse.builder().status(userDetailService.deleteUserStorage(removeRequest)).build();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/web/data/delete")
    public DeleteResponse deleteDataFromWeb(@RequestBody RemoveRequest removeRequest){
        return DeleteResponse.builder().status(userDetailService.deleteUserStorage(removeRequest)).build();
    }

}
