package com.company;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Scanner in = new Scanner(System.in);
        /*
        TODO:
            - add matching postfixes for different languages:  java -> .java;  c++ --> .cpp etc
            (for now only java)

        */
        BufferedReader br = new BufferedReader(new FileReader("params.txt"));
        String language = br.readLine();
        String path = br.readLine();
        /* TODO:
            - validation on error in input in params.txt
            - ability to pass threshold number (optionally, otherwise - some default value)
        */
        System.out.println("No errors with input.");
//        String path =  "/Users/levsvalov/IdeaProjects/PlagiarismDetectionTool/SomeAssignment";

        String command = "./moss.pl -l ";
        File file = new File(path);
        File[] files = file.listFiles();
        command = command + language + " -d ";
        for (int i = 0; i < files.length; i++) {
            File[] a = files[i].listFiles();
            if (a != null) {
                for (int j = 0; j < a.length; j++) {
                    if (a[j].getName().endsWith(".java")) {
                        // here dictionary is needed for postfixes for appropriate language

                        /*if (files[i].getName().contains(" ")){
                            String old = path + '/' + '"' +  files[i].getName() + '"';
                            String n = path + "/" +  files[i].getName().replace(" ", "_");
                            rt.exec("mv " + old + " " + n);
                            System.out.println(files[i].getName());
                        }*/
                        command += path + "/" + files[i].getName() + "/" + a[j].getName() + " ";
                    }
                }
            }
        }

        System.out.println("The MOSS command:");
        System.out.println(command);
        Process proc = rt.exec(command);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String s = null;
        String urlMoss = "";
        List<String> list = new LinkedList<>();
        while ((s = stdInput.readLine()) != null) {
            list.add(s);
        }
        urlMoss = list.get(list.size() - 1);
        String commandJplag = "java -jar jplag-2.11.8-SNAPSHOT-jar-with-dependencies.jar -l java17 -s " + path;
        proc = rt.exec(commandJplag);
        stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        ArrayList<String> arrJplag = new ArrayList<>();
        while ((s = stdInput.readLine()) != null) {
            if (s.startsWith("Comparing")) {
                arrJplag.add(s);
            }
        }
        FileWriter wr = new FileWriter(new File("result", "report.txt"));
        wr.write(urlMoss + "\n");

        wr.close();

//        System.out.println(urlMoss);

        String GenerateReport = "python3 create_main_report.py";
        Process pr = rt.exec(GenerateReport);
        BufferedReader inpt = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        System.out.println("Here is the standard output of the command:\n");
        String a = null;
        while ((a = inpt.readLine()) != null) {
            System.out.println(a);
        }
    }
}
