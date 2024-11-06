package com.bicart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="role")
@Getter
@Setter
@Builder
public class Role extends BaseEntity {
    @Id
    private String id;
    private String roleName;
    private String description;
}
