package com.bicart.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.bicart.constant.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
@Getter
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


   @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinColumn(name = "cart_id")
   private Cart cart;

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinColumn(name = "address_id")
   private Set<Address> addresses;

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinColumn(name = "order_id")
   private Set<Order> orders;

   @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @Fetch(FetchMode.JOIN)
   @JoinTable(
           name = "user_role",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   private Set<Role> roles;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return Collections.singleton(new SimpleGrantedAuthority("Employee"));
   }

   @Override
   public String getUsername() {
      return this.email;
   }

}
