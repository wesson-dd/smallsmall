package com.small.leetcode;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;

/**
 * 大顶堆的数组实现
 *
 * @author wesson
 * Created on 2024/9/7 23:44
 **/
@Data
public class MaxHeap {

    private int[] heap;
    private int size;
    private int capacity;

    public MaxHeap(int capacity) {
        this.heap = new int[capacity];
        this.size = 0;
        this.capacity = capacity;
    }

    // 添加元素到堆中
    public void add(int value) {
        if (size < capacity) {
            heap[size] = value;
            shiftUp(size);
            size++;
        } else {
            if (value < heap[0]) {
                swapMax(value);
            }
        }
    }


    public void swapMax(int value) {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        heap[0] = value;
        shiftDown(0);
    }

    // 向上调整元素
    private void shiftUp(int index) {
        while (index > 0 && heap[parent(index)] < heap[index]) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    // 向下调整元素
    private void shiftDown(int index) {
        int largest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && heap[left] > heap[largest]) {
            largest = left;
        }

        if (right < size && heap[right] > heap[largest]) {
            largest = right;
        }

        if (largest != index) {
            swap(index, largest);
            shiftDown(largest);
        }
    }

    // 获取父节点索引
    private int parent(int index) {
        return (index - 1) / 2;
    }

    // 获取左孩子节点索引
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    // 获取右孩子节点索引
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    // 交换两个位置上的元素
    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public static void main(String[] args) {
        ArrayList<Integer> integers = Lists.newArrayList(10, 33, 2, 22, 346, 78, 98, 6, 8);
        MaxHeap maxHeap = new MaxHeap(5);
        for (Integer integer : integers) {
            maxHeap.add(integer);
        }

        System.out.println(maxHeap);
    }


}
