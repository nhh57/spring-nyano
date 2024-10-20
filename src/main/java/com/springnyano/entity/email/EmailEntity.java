package com.springnyano.entity.email;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailEntity {

    private String toEmail;
    private String subject;
    private String messageBody;
    private String attachment;


}
