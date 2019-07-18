package com.example.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenRequestDto {

    private String tokenId;
    private String journeyType;
    @JsonRawValue
    private String appData;
    private int expiryOffsetInSeconds;
    private Boolean checkThreshold;

    @JsonIgnore
    public boolean isBlank() {
        return StringUtils.isEmpty(tokenId) && StringUtils.isEmpty(journeyType) && StringUtils.isEmpty(appData)
                && expiryOffsetInSeconds == 0 && checkThreshold == null;
    }


}
