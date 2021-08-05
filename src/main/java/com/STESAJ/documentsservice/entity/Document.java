package com.STESAJ.documentsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name= "documents")
@AllArgsConstructor @NoArgsConstructor @Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private long document_id;
    @NotEmpty(message = "El autor no debe estar vacio")
    private String autor;

    @Transient
    private File file;

    //fecha y archivo

}
