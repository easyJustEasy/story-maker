package org.example.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class DocWrapper {
    private String title;
    private DocDTO docDTO;
}
