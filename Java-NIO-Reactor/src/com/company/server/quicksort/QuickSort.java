package com.company.server.quicksort;

public interface QuickSort {

    public void quicksort(int[] array, int left, int right);

    default void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    default int choosePivot(int[] arr, int left, int right) {
        int mid = (left + right) / 2;
        if (arr[mid] < arr[left]) {
            swap(arr, left, mid);
        }
        if (arr[mid] > arr[right]) {
            swap(arr, mid, right);
        }
        return arr[mid];
    }

    default int partition(int[] arr, int left, int right) {
        int pivot = choosePivot(arr, left, right - 1);
        while (left <= right) {
            while (arr[left] < pivot) {
                left++;
            }
            while (arr[right] > pivot) {
                right--;
            }
            if (left <= right) {
                swap(arr, left, right);
                left++;
                right--;
            }
        }
        return left;
    }


}
