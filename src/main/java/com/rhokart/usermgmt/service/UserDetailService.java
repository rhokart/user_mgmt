package com.rhokart.usermgmt.service;


import com.rhokart.datamodel.util.Constants;
import com.rhokart.usermgmt.document.BusinessDetail;
import com.rhokart.usermgmt.document.DeleteRequest;
import com.rhokart.usermgmt.document.User;
import com.rhokart.usermgmt.document.UserAddress;
import com.rhokart.usermgmt.dto.Address;
import com.rhokart.usermgmt.dto.RegistrationInfo;
import com.rhokart.usermgmt.dto.UpdatedUser;
import com.rhokart.usermgmt.dto.UserDetail;
import com.rhokart.usermgmt.dto.request.RemoveRequest;
import com.rhokart.usermgmt.exceptation.BadRequestData;
import com.rhokart.usermgmt.repository.DeleteRequestRepo;
import com.rhokart.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteRequestRepo deleteRequestRepo;


    public UpdatedUser doUserRegistrationUpdate(RegistrationInfo registrationInfo){
        Optional<User> user  = userRepository.findById(registrationInfo.getUserId());
        if (!user.isPresent()){
            log.info("User is not present , userId:{}, please send correct user",registrationInfo.getUserId());
            return null;
        }
        // update fields as per input send.
        Boolean isUpdated = Boolean.TRUE;

        if(validateRegistrationInformation(registrationInfo)) {
            try {
                userRepository.save(userDetailsUpdateMapper(user.get(),registrationInfo));
            }catch (Exception ex){
                log.info("Update user fail , error :{}",ex.getMessage());
                isUpdated = Boolean.FALSE;
            }
        }else{
            log.info("input send for the user is incorrect.");
            throw new BadRequestData("User update information is incorrect...","Please correct the user input and send again...");
        }

        return  UpdatedUser.builder()
                .userId(user.get().getUserId())
                .isUpdated(isUpdated)
                .build();
    }

    public UserDetail getUserDetailByUserId(String userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            log.info("user details present ::{}",user.get());
            BusinessDetail businessDetail;
            if(!Objects.isNull(user.get().getBusinessDetail()))
                businessDetail = BusinessDetail.builder()
                        .businessName(user.get().getBusinessDetail().getBusinessName())
                        .businessPan(user.get().getBusinessDetail().getBusinessPan())
                        .gstNumber(user.get().getBusinessDetail().getGstNumber())
                        .build();
            else
                businessDetail = BusinessDetail.builder().build();

            Address address;
            if(!Objects.isNull(user.get().getUserAddress()))
                address = Address.builder()
                        .addressLine1(user.get().getUserAddress().getAddressLine1())
                        .addressLine2(user.get().getUserAddress().getAddressLine2())
                        .city(user.get().getUserAddress().getCity())
                        .district(user.get().getUserAddress().getDistrict())
                        .pinCode(user.get().getUserAddress().getPincode())
                        .state(user.get().getUserAddress().getState())
                        .build();
            else
                address = Address.builder().build();

            return UserDetail.builder()
                    .userId(user.get().getUserId())
                    .email(user.get().getEmail())
                    .firstName(user.get().getFirstName())
                    .lastName(user.get().getLastName())
                    .gender(user.get().getGender())
                    .primaryMob(user.get().getPrimaryMob())
                    .secondaryMob(user.get().getSecondaryMob())
                    .businessDetail(businessDetail)
                    .profilePictureUri("")
                    .address(address)
                    .isProfileCompleted(user.get().isProfileCompleted())
                    .build();
        }

        return UserDetail.builder().build();
    }

    private boolean validateRegistrationInformation(RegistrationInfo info){

        if(StringUtils.isEmpty(info.getFirstName()))
            return Boolean.FALSE;

        if(StringUtils.isEmpty(info.getEmail()))
            return Boolean.FALSE;

        if(StringUtils.isEmpty(info.getPrimaryMob()))
            return Boolean.FALSE;

        if(StringUtils.isEmpty(info.getUserAddress().getAddressLine1()))
            return Boolean.FALSE;

        if(StringUtils.isEmpty(info.getUserAddress().getPinCode()))
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    private User userDetailsUpdateMapper(User user,RegistrationInfo registrationInfo){

        user.setPrimaryMob(registrationInfo.getPrimaryMob());
        user.setUserAddress(UserAddress.builder()
                .pincode(registrationInfo.getUserAddress().getPinCode())
                .state(registrationInfo.getUserAddress().getState())
                .city(registrationInfo.getUserAddress().getCity())
                .addressLine2(registrationInfo.getUserAddress().getAddressLine2())
                .addressLine1(registrationInfo.getUserAddress().getAddressLine1())
                .district(registrationInfo.getUserAddress().getDistrict())
                .build());

        user.setBusinessDetail(BusinessDetail.builder()
                .businessPan(registrationInfo.getBusinessInfo().getBusinessPan())
                .businessName(registrationInfo.getBusinessInfo().getBusinessName())
                .gstNumber(registrationInfo.getBusinessInfo().getGstNumber())
                .build());

        user.setFirstName(registrationInfo.getFirstName());
        user.setLastName(registrationInfo.getLastName());
        user.setGender(registrationInfo.getGender());
        user.setSecondaryMob(registrationInfo.getSecondaryMob());
        user.setProfileCompleted(Boolean.TRUE);
        user.setDefaultPincode(registrationInfo.getUserAddress().getPinCode());

        return user;
    }

    public String deleteUserStorage(RemoveRequest removeRequest){
        deleteRequestRepo.save(new DeleteRequest(removeRequest.getName(), removeRequest.getEmail(),removeRequest.getMobile()));
        return "Delete request is successful";
    }
}
