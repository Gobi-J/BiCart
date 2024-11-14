package com.bicart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
   @GeneratedValue(strategy = GenerationType.UUID)
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
