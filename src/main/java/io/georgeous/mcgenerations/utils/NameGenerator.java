package io.georgeous.mcgenerations.utils;

public class NameGenerator {

    public static final String[] firstNames = {"Martha", "Lisa", "Julia", "Sarah", "Anna", "Maria", "Emma", "Sophia", "Charlotte", "Mia", "Chloe", "Victoria", "Scarlett", "Amy", "Madison", "Ellie", "Natalie", "Adele", "Joy", "Olivia", "Naomi", "Grace", "Ruby", "Quinn", "Lydia", "Vivian", "Hailey", "Autumn", "Svenja", "Aurora", "Hazel", "Penelope"};
    public static final String[] lastNames = {"Marten", "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Taylor", "Moore", "White", "Walker", "Hill", "Nelson", "Baker", "Adams", "Mitchell", "Clinton", "Bush", "Merkel", "Musk", "Einstein", "Simpson", "Jefferson", "Rogan", "Ronan", "Ericson", "Bjornson", "Mueller", "Freundl", "Goldberg", "Lovejoy"};

    private static String randomName(String[] list) {
        int i = (int) (Math.random() * (list.length - 1));
        return list[i];
    }

    public static String randomFirst() {
        return randomName(firstNames);
    }

    public static String randomLast() {
        return randomName(lastNames);
    }
}