package com.yadev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LocaleInfo {

    private String lang;
    private String description;
}
