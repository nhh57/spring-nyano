package com.springnyano.entity.email;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class EmailEntity {

    private String toEmail;
    private String subject;
    private String messageBody;
    private String attachment;


}
