package com.example.technomarket.model.pojo;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String imageName;
}
