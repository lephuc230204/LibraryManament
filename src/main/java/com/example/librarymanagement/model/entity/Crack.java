package com.example.librarymanagement.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table( name = "crack")
public class Crack {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    private String shelf;
    private int row;
    private int slot;

}
