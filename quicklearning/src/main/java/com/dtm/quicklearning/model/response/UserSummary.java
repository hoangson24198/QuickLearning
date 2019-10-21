package com.dtm.quicklearning.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSummary {
    private Integer id;
    private String name;
    private String email;
}
