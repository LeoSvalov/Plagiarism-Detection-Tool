package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Scanner in = new Scanner(System.in);
        /*
        TBD:
            add matching postfixes for different languages:  java -> .java;  c++ --> .cpp etc
            (for now only java)

        */
        String command = "./moss.pl -l ";
        // here reading
        System.out.println("Input the language:");
        String language = in.next();
        System.out.println("Input the absolute path to the solutions:");
        String path = in.next();
//        String path =  "/Users/levsvalov/IdeaProjects/PlagiarismDetectionTool/SomeAssignment";
        File file = new File(path);
        File[] files = file.listFiles();
        command = command +  language + " -d ";
        for (int i=0;i<files.length;i++){
            File[] a = files[i].listFiles();
            if (a!=null) {
                for (int j = 0; j < a.length; j++) {
                    if (a[j].getName().endsWith(".java")) { // here dictionary is needed for postfixes for appropriate language

                    }
                    command += path + "/" + files[i].getName() + "/" + a[j].getName() + " ";
                }
            }
        }
        System.out.println(command);
        Process proc = rt.exec(command);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String s = null;
        String urlMoss = "";
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
//            urlMoss =  s;
        }
        System.out.println("-------------");

        String commandJplag = "java -jar jplag-2.11.8-SNAPSHOT-jar-with-dependencies.jar -l java17 -s " +  path;
        proc = rt.exec(commandJplag);
        stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

       /* System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }*/

    }
}
