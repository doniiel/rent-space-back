package com.rentspace.search_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@Document(indexName = "listings")
public class Listing {
    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String country;

    @Field(type = FieldType.Double)
    private BigDecimal latitude;

    @Field(type = FieldType.Double)
    private BigDecimal longitude;

    @Field(type = FieldType.Text)
    private String type;

    @Field(type = FieldType.Integer)
    private Integer maxGuests;

    @Field(type = FieldType.Double)
    private Double pricePerNight;
}
