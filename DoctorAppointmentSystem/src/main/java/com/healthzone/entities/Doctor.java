package com.healthzone.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Doctor {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String name;
	
	    @Column(nullable = false)
	    private String specialty;

	    @Column(nullable = false, length = 1000)
	    private String about;

		public Doctor() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSpecialty() {
			return specialty;
		}

		public void setSpecialty(String specialty) {
			this.specialty = specialty;
		}

		

		public String getAbout() {
			return about;
		}

		public void setAbout(String about) {
			this.about = about;
		}

		@Override
		public String toString() {
			return "Doctor [id=" + id + ", name=" + name + ", specialty=" + specialty + ", about=" + about + "]";
		}

		
		
	    
	    

}
