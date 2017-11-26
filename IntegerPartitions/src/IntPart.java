

import java.util.*;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class IntPart {

    public static void main(String[] args){

        System.out.println(IntPart.part( 50));

        //2916

        //133
    }



    public static Set<List<Long>> usedCombinations = new HashSet<>();

//    public static Set<Map<Long, Integer>> usedCombinations = new HashSet<>();

    public static Map<Long, List<List<Long>>> partitionsForInt = new Hashtable<>();

    static long numberToPartition = 0;

    public static String part(long n) {

        numberToPartition = n;
        List<List<Long>> partitionedArrays = new ArrayList<>();

        partitionedArrays.add(Arrays.asList(n));


        for(List<Long> completeList : partitionsOf(n)){
            partitionedArrays.add(completeList);
        }



        List<Long> product = partitionedArrays.stream()
                .map(array -> array.stream().reduce(new Long(1) , (a,b) -> a*b))
                .distinct()
                .collect(Collectors.toList());


//        System.out.println(product);
        Collections.sort(product);
//        System.out.println(product);

        int size = product.size();
        long range = product.get(size - 1) - product.get(0);
        double average = product.stream()
                .reduce(0L, (a,b) -> a+b) / (double)size;

        double median = 0;

        if((size % 2) != 0){
            median = (double)product.get(size/2);
        }else{
            median = (double)(product.get(size/2) + product.get(size/2 - 1)) / 2.0;
        }

//        System.out.printf("Range: %d Average: %.2f Median: %.2f", range ,average, median);


        DecimalFormat doubleFormatter = new DecimalFormat("#.00");
        String rangeAsString = "" + range;
        String averageAsString = doubleFormatter.format(average);
        String medianAsString = doubleFormatter.format(median);


        return "Range: " + rangeAsString + " Average: " + averageAsString + " Median: " + medianAsString;
    }




    //MARK: Method of interest

    public static List<List<Long>> partitionsOf(long n){

        List<List<Long>> subPartitionedArrays = new ArrayList<>();

//        System.out.println("n value: " + n);

        if(returnRegister(n).isPresent()){
//            System.out.println("Using saved values for n = " + n);

            if(n == numberToPartition){

                List<List<Long>> filteredPartitionArray = new ArrayList<>();

                for(List<Long> partitions : returnRegister(n).get()){

                    if(compareToUsedCombinations(partitions)){
                        filteredPartitionArray.add(partitions);
                    }

                }

                return filteredPartitionArray;
            }

            return returnRegister(n).get();
        }

//
//        else{
//            System.out.println("No saved values for n = " + n);
//        }

        for(long i = 1; i <= n/2; i++){

            subPartitionedArrays.add(Arrays.asList(n - i, i));
//            System.out.println("Adding n-i, i : [" + (n-i) + " , " + i + "]");

            if(n == numberToPartition){
                compareToUsedCombinations(Arrays.asList(n - 1, i));
                if((2*i) >= (n+1-i)){
//                    System.out.println("jumping out here...");
                    continue;
                }
            }

            for(List<Long> partitions : partitionsOf(n - i)){

                List<Long> addToSubPartitions = new ArrayList<>();

                long sum = 0;


                for(Long number : partitions){
                    sum += number;
                    addToSubPartitions.add(number);
                }
                sum += i;
                addToSubPartitions.add(i);

                if(sum == numberToPartition){
                    if(compareToUsedCombinations(addToSubPartitions)){
//                        Collections.sort(addToSubPartitions, Comparator.reverseOrder());
                        subPartitionedArrays.add(addToSubPartitions);
//                        System.out.println("Added " + addToSubPartitions + " in parent list " + (n - i)  + " and " + i);
                    }
//                    else{
//                        System.out.println("Combination" + addToSubPartitions + " not used in parent list " + (n - i) + " and " +(i));
//                    }
                }else{
                    subPartitionedArrays.add(addToSubPartitions);
                }
            }
        }


        registerReturn(subPartitionedArrays, n);
        return subPartitionedArrays;
    }




    private static Optional<List<List<Long>>> returnRegister(long forValue){

        return Optional.ofNullable(partitionsForInt.get(forValue));

    }


    private static void registerReturn(List<List<Long>> ofLists, long forValue){

        List<List<Long>> noDuplicateList;

        Set<List<Long>> combinationChecker = new HashSet<>();

        for(List<Long> list : ofLists) {
            Collections.sort(list);
            combinationChecker.add(list);
        }

        noDuplicateList = new ArrayList<>(combinationChecker);
        partitionsForInt.put(forValue, noDuplicateList);

    }




    public static boolean compareToUsedCombinations(List<Long> usedCombinationArray){

        Collections.sort(usedCombinationArray);
        return usedCombinations.add(usedCombinationArray);
    }



//    private static void registerReturn(List<List<Long>> ofLists, long forValue){
//
//        List<List<Long>> noDuplicateList = new ArrayList<List<Long>>();
//
//        Set<Map<Long, Integer>> combinationChecker = new HashSet<>();
//
//
//
//        for(List<Long> listInList: ofLists) {
//
//            Map<Long, Integer> map = new Hashtable<>();
//
//            for (Long number : listInList) {
//                if (map.containsKey(number)) {
//                    map.put(number, map.get(number) + 1);
//                } else {
//                    map.put(number, 1);
//                }
//            }
//
//            if(combinationChecker.add(map)){
//                noDuplicateList.add(listInList);
//            }
//
//        }
//
//
//        noDuplicateList.add(Arrays.asList(forValue));
//        System.out.print("Saving value of " + forValue + "  ");
//        System.out.println(noDuplicateList);
//        partitionsForInt.put(forValue, noDuplicateList);
//
//    }


//    public static boolean compareToUsedCombinations(List<Long> usedCombinationArray){
//
//        Map<Long, Integer> map = new Hashtable<>();
//
//        for(Long number : usedCombinationArray){
//            if(map.containsKey(number)){
//                map.put(number, map.get(number) + 1);
//            }else{
//                map.put(number, 1);
//            }
//        }
//
//        return usedCombinations.add(map);
//    }

}
