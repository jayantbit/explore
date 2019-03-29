package com.explore;

public enum PromoType {
  Single("Single"),Bundle("Bundle"),Multi("Multi"),Combo("Combo"),MultiGroups("Multi - Groups");

  String name;


  PromoType(String name)
  {
    this.name = name;
  }

}
