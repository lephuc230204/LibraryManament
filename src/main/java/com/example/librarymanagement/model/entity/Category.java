package com.example.librarymanagement.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table( name = "Categories")
@Entity
public class Category {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;
}
