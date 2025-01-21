package cibernarium.virtualPetBackEnd.user;


import cibernarium.virtualPetBackEnd.pet.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
// UniqueConstraint impide que haya duplicados
public class User implements UserDetails {

    @Id
    @GeneratedValue
    Long id;
    @Basic
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String password;

    String firstname;
    String lastname;
    String country;
//    private List<Role> roles;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;
    // Esto hace que el enum (Que por defecto se guarda como su valor numérico, se guarde como un string en este caso)

    @Enumerated(EnumType.ORDINAL)  // Almacena el ordinal (índice) del enum
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
        return true;
    }
}
