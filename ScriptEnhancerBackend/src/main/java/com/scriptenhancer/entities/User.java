package com.scriptenhancer.entities;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "signup_users")
public class User {

    @Id
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(name = "signupuser_roles", joinColumns = @JoinColumn(name = "user_email", referencedColumnName = "email"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_inputs_outputs" , joinColumns = @JoinColumn(name = "user_email" , referencedColumnName = "email") , inverseJoinColumns = @JoinColumn(name = "nput_output_id"))
    private List<InputAndOutput> inputAndOutputs;

}
