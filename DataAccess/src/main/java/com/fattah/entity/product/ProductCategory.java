package com.fattah.entity.product;

import com.fattah.entity.file.File;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500,nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String description;

    private Boolean enabled=true;

    @ManyToOne
    @JoinColumn(nullable = false)
    private File image;
}
