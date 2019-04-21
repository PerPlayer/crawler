package bak;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/6/1.
 */
public class SortTest {

    public static final Random RANDOM = new Random(10000000);

    public static void main(String[] args){

        int capacity = 10000000;
        List<Integer> nums = new ArrayList<>(capacity);
        for(int i = 0; i < capacity; i++) {
            nums.add(Math.abs(RANDOM.nextInt()));
        }

        long l1 = System.currentTimeMillis();
        quickSort(nums);
        System.out.println(System.currentTimeMillis() - l1);
        long l2 = System.currentTimeMillis();
        mergeSort(nums);
        System.out.println(System.currentTimeMillis() - l2);
        long l3 = System.currentTimeMillis();
        bitSort(nums);
        System.out.println(System.currentTimeMillis() - l3);
    }

    public static List<Integer> bitSort(List<Integer> nums) {
        if (nums.size() < 2) {
            return nums;
        }
        BitSet bits = new BitSet();
        for (int num : nums) {
            if (num < 0) {
                System.out.println(num);
            }
            bits.set(num);
        }
        ArrayList<Integer> lists = new ArrayList<>();
        for(int i = 0; i < bits.size(); i++) {
            if (bits.get(i)) {
                lists.add(i);
            }
        }
        return nums;
    }

    public static List<Integer> mergeSort(List<Integer> nums){
        if (nums.size() < 2) {
            return nums;
        }
        int i = (int)Math.floor((nums.size())/2);
        List<Integer> lNums = new ArrayList<>(nums.subList(0, i));
        List<Integer> rNums = new ArrayList<>(nums.subList(i, nums.size()));
        return merge(mergeSort(lNums), mergeSort(rNums));
    }

    public static List<Integer> merge(List<Integer> llist, List<Integer> rlist){
        ArrayList<Integer> list = new ArrayList<>(llist.size() + rlist.size());
        int li = 0, ri = 0;
        while(true){
            if (li == llist.size()) {
                list.addAll(rlist.subList(ri, rlist.size()));
                break;
            }
            if (ri == rlist.size()) {
                list.addAll(llist.subList(li, llist.size()));
                break;
            }
            int lnum = llist.get(li++);
            int rnum = rlist.get(ri++);
            if (lnum < rnum) {
                list.add(lnum);
                ri--;
            }else {
                list.add(rnum);
                li--;
            }
        }
        return list;
    }

    public static List<Integer> quickSort(List<Integer> nums){
        if (nums.size() < 2) {
            return nums;
        }
        List<Integer> leftNums = new ArrayList<Integer>();
        List<Integer> rightNums = new ArrayList<Integer>();
        for (int i = 1; i < nums.size();i++) {
            int num = nums.get(i);
            if (num < nums.get(0)) {
                leftNums.add(num);
            }else{
                rightNums.add(num);
            }
        }
        List<Integer> reNums = quickSort(leftNums);
        reNums.add(nums.get(0));
        reNums.addAll(quickSort(rightNums));
        return reNums;
    }
}
