package com.explore.kafka;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Dummy {
 private int id;
 private String name;
 private Date date;
}
