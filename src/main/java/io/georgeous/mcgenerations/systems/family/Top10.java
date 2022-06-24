package io.georgeous.mcgenerations.systems.family;

public class Top10 {

    private static Top10 instance;


    private Family[] currentTop10 = new Family[10];
    private Family[] allTimeTop10 = new Family[10];

    private Top10(){

    }

    public static Top10 get(){
        if(instance == null){
            instance = new Top10();
        }
        return instance;
    }

    public Family[] getCurrentTop10(){
        return currentTop10;
    }

    public Family[] getAllTimeTop10(){
        return allTimeTop10;
    }

    public void update(Family family){
        for (int i = currentTop10.length - 1; i >= 0; i--) {
            if(currentTop10[i] == null || family.getMaxGenerations() > currentTop10[i].getMaxGenerations()){
                Family temp = currentTop10[i];
                currentTop10[i] = family;
                if( i + 1 < currentTop10.length){
                    currentTop10[i+1] = temp;
                }

            }
        }

    }
}
