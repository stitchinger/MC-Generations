package io.georgeous.mcgenerations.utils;

public class NameGenerator {

    public static final String[] firstNames = {"Martha","Lisa","Julia","Sarah","Anna","Maria","Emma","Sophia","Charlotte","Mia","Chloe","Victoria","Scarlett", "Amy", "Madison", "Ellie", "Natalie"};
    public static final String[] lastNames = {"Smith","Johnson","Williams","Brown","Jones","Garcia","Miller","Davis","Taylor","Moore","White","Walker","Hill","Nelson","Baker","Adams", "Mitchell"};


    private static String randomName(String[] list){
        int i = (int) (Math.random() * (list.length - 1));
        return list[i];
    }

    public static String randomFirst(){
        return randomName(firstNames);
    }

    public static String randomLast(){
        return randomName(lastNames);
    }

}
