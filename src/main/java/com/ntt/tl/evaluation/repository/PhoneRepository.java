package com.ntt.tl.evaluation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntt.tl.evaluation.entity.UsersPhoneEntity;

@Repository
public interface PhoneRepository extends JpaRepository<UsersPhoneEntity, Integer> {

	
	@Query("SELECT up FROM UsersPhoneEntity up WHERE up.phoneNumber = :phoneNumber "
			+ "AND up.cityCode = :cityCode "
			+ "AND up.countryCode = :countryCode "
			+ "AND up.user.idUser = :userId")
	Optional<UsersPhoneEntity>  existsByPhoneNumberCityCodeAndCountryCode(
    		@Param("phoneNumber") String phoneNumber, 
    		@Param("cityCode") String cityCode, 
    		@Param("countryCode") String countryCode, 
    		@Param("userId") String userId);
	
	
	@Query("SELECT up FROM UsersPhoneEntity up WHERE  up.id = :idPhone "
			+ "AND up.user.idUser = :userId")
	Optional<UsersPhoneEntity>  existsByUserIdAndPhoneId(
    		@Param("idPhone") Integer idPhone, 
    		@Param("userId") String userId);
	
	
}
