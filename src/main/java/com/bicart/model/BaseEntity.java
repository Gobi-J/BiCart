package com.bicart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
    private boolean isDeleted;
}
