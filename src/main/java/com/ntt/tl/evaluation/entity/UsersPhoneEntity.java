package com.ntt.tl.evaluation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author avenegas
 *
 */
@Entity
@Table(name = "tb_Users_Phone")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersPhoneEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_user_phone")
	private Integer id;
	
	@Column(name="phoneNumber", nullable = false, length = 9)
	private String phoneNumber;
	
	@Column(name="cityCode", nullable = false, length = 2)
	private String cityCode;
	
	@Column(name="countryCode", nullable = false, length = 2)
	private String countryCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", referencedColumnName = "id_user", insertable = true, updatable = true)
    private UsersEntity user;
	

}
