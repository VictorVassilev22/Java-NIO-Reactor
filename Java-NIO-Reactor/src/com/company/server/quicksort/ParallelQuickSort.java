package com.company.server.quicksort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ParallelQuickSort extends RecursiveAction implements QuickSort {

    int left, right;
    int[] arr;
    private final int threshold = 200;

    public ParallelQuickSort(int left, int right, int[] arr) {
        this.arr = arr;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (left < right) {
            if ((right - left) >= threshold) {
                ForkJoinTask.invokeAll(createSubtasks());
            } else {
                bubbleSort(arr, left, right);
            }
        }
    }

    private List<ParallelQuickSort> createSubtasks() {
        List<ParallelQuickSort> subtasks = new ArrayList<>();
        int index = partition(arr, left, right);
        subtasks.add(new ParallelQuickSort(left, index - 1, arr));
        subtasks.add(new ParallelQuickSort(index, right, arr));

        return subtasks;
    }

    @Override
    public void quicksort(int[] array, int left, int right) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new ParallelQuickSort(0, arr.length - 1, arr));
        pool.shutdown();
    }

    public void bubbleSort(int arr[], int left, int right) {
        int i, j;
        boolean swapped;
        for (i = left; i < right; i++) {
            swapped = false;
            for (j = left; j < right - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j+1);
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }
}
