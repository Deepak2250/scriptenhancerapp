package com.scriptenhancer.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "input_output")
public class InputAndOutput  {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	 
	 @Lob
	 @Column(nullable = true , columnDefinition = "LONGTEXT")
	private String inputs;
	 
	 @Lob
	 @Column(nullable = true , columnDefinition = "LONGTEXT")
	 private String outputs;
	
	 @JsonBackReference
	 @ManyToMany(mappedBy = "inputAndOutputs" , fetch = FetchType.LAZY)
	private List<User> user;
}