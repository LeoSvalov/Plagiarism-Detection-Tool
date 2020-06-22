package com.company;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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
        /*
        // here reading
        System.out.println("Input the language:");
        String language = in.next();
        System.out.println("Input the absolute path to the solutions:");
        String path = in.next();
        String path =  "/Users/levsvalov/IdeaProjects/PlagiarismDetectionTool/SomeAssignment";
        */
        String command = "./moss.pl -l ";
        File file = new File(path);
        File[] files = file.listFiles();
        command = command +  language + " -d ";
        for (int i=0;i<files.length;i++){
            File[] a = files[i].listFiles();
            if (a!=null) {
                for (int j = 0; j < a.length; j++) {
                    if (a[j].getName().endsWith(".java")) {
                        // here dictionary is needed for postfixes for appropriate language


                        command += path + "/" + files[i].getName() + "/" + a[j].getName() + " ";
                    }
                }
            }
        }


//        System.out.println(command);
        Process proc = rt.exec(command);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String s = null;
        String urlMoss = "";

        while ((s = stdInput.readLine()) != null) {
            urlMoss =  s;
        }

        String commandJplag = "java -jar jplag-2.11.8-SNAPSHOT-jar-with-dependencies.jar -l java17 -s " +  path;
        proc = rt.exec(commandJplag);
        stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        ArrayList<String> arrJplag = new ArrayList<>();
        while ((s = stdInput.readLine()) != null) {
            if(s.startsWith("Comparing")){
                arrJplag.add(s);
            }
        }
        FileWriter wr = new FileWriter(new File("result", "indexGeneral.txt"));
        wr.write(urlMoss + "\n");
        wr.write("\n");
        for (String a: arrJplag ){
            wr.write(a+"\n");
        }
        wr.close();
        System.out.println("The results are stored in folder /result/");
        /*
        System.out.println("General numbers are here:");
        System.out.println(urlMoss);
        for (String a: arrJplag ){
            System.out.println(a);
        }
        */
    }
}
