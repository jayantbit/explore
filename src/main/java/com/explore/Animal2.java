package com.explore;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

@Builder
@Value  //makes it immutable
@Accessors(fluent = true)
public class Animal2 {
   String name;
   int numberOfLegs;
}
