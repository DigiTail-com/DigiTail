package com.digitail.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usr")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 30, message = "Username should be between 2 and 30 characters")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 3, message = "Password be more than 3 characters")
    private String password;

    private String firstName;

    private String secondName;

    private boolean active;

    private Float money;

    @Email(message = "Email should be valid")
    private String email;

    @OneToOne(mappedBy="user", fetch = FetchType.EAGER)
    private Card card;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private Set<Product> products;

    @OneToOne(mappedBy="user", fetch = FetchType.EAGER)
    private BasketGoods basketGoods;

    private Float basketCosts;

    public void addProduct(Product product){
        products.add(product);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public boolean isActive() {
        return active;
    }

    public String toString(){
        return "";
    }

}