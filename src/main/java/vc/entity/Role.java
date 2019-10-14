package vc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@Column(name = "id_role", nullable = false)
	private Integer idRole;

	@Column(name = "role_name", length = 20, nullable = false)
	private String roleName;
}