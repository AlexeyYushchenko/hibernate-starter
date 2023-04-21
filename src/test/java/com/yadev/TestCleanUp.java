package com.yadev;

import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Data
public class TestCleanUp {
    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    }
}
