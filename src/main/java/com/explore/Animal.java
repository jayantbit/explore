package com.explore;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class Animal {

   static Animal create(String name, int numberOfLegs) {


      return new com.explore.AutoValue_Animal(name, numberOfLegs);
   }

   abstract String name();
   abstract int numberOfLegs();
}
