package com.bicart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
@Getter
@Setter
@Builder
public class User extends BaseEntity implements UserDetails {

   @Id
   private String id;

   @Column(nullable=false)
   private String name;

   @Column(name = "mobile_number", unique = true, nullable = false)
   private String mobileNumber;

   @Column(unique = true, nullable=false)
   private String email;

   @Column(unique = true, nullable=false)
   private String password;

   @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
   @JoinTable(
           name = "user_role",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   private Set<Role> role;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role r : role) {
             authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        }
        return authorities;
   }

   @Override
   public String getUsername() {
      return this.email;
   }

}
