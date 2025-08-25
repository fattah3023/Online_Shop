package com.fattah.entity.site;

import com.fattah.entity.enums.BlogStatus;
import com.fattah.entity.file.File;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000,nullable = false)
    private String title;

    @Column(length = 1000,nullable = false)
    private String subTitle;

    private LocalDateTime publishDate;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String description;

    private BlogStatus status;

    private Long visitCount;

    @ManyToOne
    @JoinColumn(nullable = false)
    private File image;

}
