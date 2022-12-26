package com.JUnit5Mockito.JUnit5Mockito;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

}
