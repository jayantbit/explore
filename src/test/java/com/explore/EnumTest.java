package com.explore;

import org.junit.Test;

import java.util.Arrays;

public class EnumTest {

  @Test
  public void test()
  {
    boolean flag= Arrays.stream(PromoType.values()).anyMatch(promoType -> promoType.name.equals("Multi - Groups1"));

    System.out.println(flag);
  }
}
