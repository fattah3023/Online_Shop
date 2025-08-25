package com.fattah.entity.site;

import com.fattah.entity.file.File;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,nullable = false)
    private String title;

    @Column(length = 1000,nullable = false)
    private String link;

    private Integer orderNumber;

    private Boolean enabled=true;

    @ManyToOne
    @JoinColumn(nullable = false)
    private File image;
}
