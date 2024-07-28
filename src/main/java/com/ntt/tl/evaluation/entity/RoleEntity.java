package com.ntt.tl.evaluation.entity;

import com.ntt.tl.evaluation.constant.ERoleUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TB_ROLES")
public class RoleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "id_role")
	private long idRole;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "name_role")
	private ERoleUser name;

}
