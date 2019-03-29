package com.explore;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RandomTests {

 static class  Dummy
 {
   String name;
   int score;

   public Dummy(String name, int score) {
     this.name = name;
     this.score = score;
   }

   public String getName() {
     return name;
   }

   public void setName(String name) {
     this.name = name;
   }

   public int getScore() {
     return score;
   }

   public void setScore(int score) {
     this.score = score;
   }
 }

 @Test
  public void test1()
 {
   List<Dummy> list= new ArrayList<>();

   list.add(new Dummy("jayant",0));
   list.add(new Dummy("mukherji",1));

   Map<String,List<Dummy>> map1= list.stream().collect(Collectors.groupingBy(Dummy::getName));

   Map<Integer,List<Dummy>> map2= list.stream().collect(Collectors.groupingBy(Dummy::getScore));

   map1.get("jayant").get(0).setScore(100);

   System.out.println(map2.get(0).get(0).getScore());

 }


}
