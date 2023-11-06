package edu.MttqDemo.Model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse implements Serializable {
    private String advice;
}
