package com.tfa.transform;

import soot.*;

public class InvokeInstrumenter {
    public static String CLASS_SYSTEM = "java.lang.System";
    public static String CLASS_PRINT_STREAM = "java.io.PrintStream";
    public static String CLASS_FILEUTIL = "com.tfa.utils.FileUtil";

    private static InvokeInstrumenter instance = new InvokeInstrumenter();
    public static InvokeInstrumenter v() {
        return instance;
    }


    public static void main(String[] args) {
//        PackManager.v().getPack("jop").add(new Transform("jop.instrumenter", InvokeInstrumenter.v()));
        String cp = null;
        String process_dir = null;
        String output = null;
        String f = null;
        for(int i=0; i < args.length; i++) {
            switch (args[i]) {
                case "-cp": cp = args[i+1]; break;
                case "-process-dir": process_dir = args[i+1]; break;
                case "-d" : output = args[i+1]; break;
                case "-f" : f = args[i+1]; break;
                default: printHelp();return;
            }
        }
        if(cp == null) {
            System.out.println("classpath not included, please use \"-h\" to see how to specify classpath");
            return;
        }
        if(process_dir == null) {
            System.out.println("process_dir not specify, please use \"-h\" to see how to specify process_dir");
            return;
        }
        if(output == null) {
            output = "defaultOutput";
            System.out.println("output folder not specify, will generate defaultOutput folder instead");
        }
        if(f == null) {
            f = "c";
            System.out.println("output format not specify, will generate bytecode file instead");
        }

        String[] argss = new String[]{"-cp", cp, "-process-dir", process_dir, "-keep-line-number", "-allow-phantom-refs",
                "-whole-program", "-d", output, "-f", f};

        PackManager.v().getPack("wjtp").add(new Transform("wjtp.intru", OnlyVirtualCall.v()));

        Scene.v().addBasicClass(CLASS_PRINT_STREAM, SootClass.SIGNATURES);
        Scene.v().addBasicClass(CLASS_SYSTEM, SootClass.SIGNATURES);
        Scene.v().addBasicClass(CLASS_FILEUTIL, SootClass.SIGNATURES);
        soot.Main.main(argss);

    }

    private static void printHelp() {
        System.out.println("Usage: java -jar instrumenter.jar -cp classpath -process-dir target -d output_dir -f output_format");
        System.out.println("-cp classpath           include needed library");
        System.out.println("-process-dir target     specify target code your wanna instrument");
        System.out.println("-d output_dir           specify output dir");
        System.out.println("-f format               format of output file, \"c\" for class file, \"f\" for jimple file, more details please see Soot Framework");
        System.out.println("-h                      show how to use this instrumenter");
    }

}
