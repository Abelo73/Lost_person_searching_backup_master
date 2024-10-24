//package com.act.Gakos.mapper;
//
//
//import com.act.Gakos.dto.UserDTO;
//import com.act.Gakos.entity.FatherDetails;
//import com.act.Gakos.entity.MotherDetails;
//import com.act.Gakos.entity.User;
//
//public class UserMapper {
//
////    public static UserDTO toDto(User user) {
////        return new UserDTO(
////                user.getId(),
////                user.getFirstName(),
////                user.getMiddleName(),
////                user.getLastName(),
////                user.getEmail(),
////                user.getUsername(),
////                user.getPhoneNumber(),
////                user.getCreatedAt(),
////                user.getUpdatedAt(),
////                user.getRole(),
////                toFatherDetailsDto(user.getFatherDetails()),  // Map FatherDetails
////                toMotherDetailsDto(user.getMatherDetails()),  // Map MotherDetails if applicable
////                toProfileInfoDto(user.getProfileInfo()),      // Map ProfileInfo if applicable
////                toAddressDetailsDto(user.getAddressDetails()) // Map AddressDetails if applicable
////        );
////    }
//
////    private static FatherDetails toFatherDetailsDto(FatherDetails fatherDetails) {
////        if (fatherDetails == null) return null;
////        return new FatherDetails(
////                fatherDetails.getId(),
////                fatherDetails.getFirstName(),
////                fatherDetails.getLastName(),
////                fatherDetails.getPhoneNumber(),
////                fatherDetails.getUser()
////        );
////    }
////
////    private static MotherDetails toMotherDetailsDto(MotherDetails motherDetails) {
////        if (motherDetails == null) return null;
////        return new MotherDetails(
////                motherDetails.getMotherName(),
////                motherDetails.getBodyColor(),
////                motherDetails.getMotherImage(),
////                motherDetails.getId(),
////                motherDetails.getHeight(),
////                motherDetails.getOccupations(),
////                motherDetails.getCurrentAddress(),
////                motherDetails.getMotherPhoneNumber()
////        );
////    }
//
//
//
//    // Similarly add toProfileInfoDto and toAddressDetailsDto mapping methods
//}
