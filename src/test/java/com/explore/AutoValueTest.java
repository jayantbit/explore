package com.explore;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class AutoValueTest {

   @Test
  public void test1()
  {
     Animal dog = Animal.create("dog", 4);
     assertEquals("dog", dog.name());
     assertEquals(4, dog.numberOfLegs());

     // You probably don't need to write assertions like these; just illustrating.

     Animal dog2 = Animal.create("dog", 4);

     assertFalse(dog2==dog);
     assertTrue(dog2.equals(dog));

     //dog2 and dog are 2 differnt objects, but caling equals on them will give true because of autovalue
     //if not using autovalue...then u have to ovveride equals and hashcode method.
     //another option is to use lombork's @Value or @Data annotation

  }

  @Test
   public void test2()
  {
     Animal2 dog= Animal2.builder()
                     .name("dog")
                     .numberOfLegs(4)
                    .build();

     Animal2 dog2= Animal2.builder()
      .name("dog")
      .numberOfLegs(4)
      .build();

     assertFalse(dog2==dog);
     assertTrue(dog2.equals(dog));

  }
}
