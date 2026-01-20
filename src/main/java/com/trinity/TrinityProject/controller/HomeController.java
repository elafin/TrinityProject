package com.trinity.TrinityProject.controller;

import com.trinity.TrinityProject.model.ClassesForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("home1", "Witaj na stronie domowej!");
        model.addAttribute("home2", "Znajdziesz tu info o stronie.");

        ClassesForm classesForm = new ClassesForm(new ArrayList<>());
        model.addAttribute("classesForm", classesForm);

        return "home";
    }

    public static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    public static String getClass(File file){
        return removeExtension(file.getName());
    }

    class ExampleClass1 {
        private int field1;
        private double field2;
        private String field3;

        public ExampleClass1() {};

        public ExampleClass1(int field1, double field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }

        public int getField1() {
            return field1;
        }

        public void setField1(int field1) {
            this.field1 = field1;
        }

        public double getField2() {
            return field2;
        }

        public void setField2(double field2) {
            this.field2 = field2;
        }

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }
    }

    public static String checkMod(Member member) {
        String str = "";
        String mod = Modifier.toString(member.getModifiers()).split(" ")[0];
        String param = "";
        String returnType = "";

        if (member instanceof Constructor<?>) {
            param += "(";
            Constructor<?> con = (Constructor<?>) member;
            Parameter[] p = con.getParameters();

            returnType = con.getAnnotatedReturnType().toString();

            for (int i = 0; i < p.length; i++) {
                param += p[i].getType().getName() + " " + p[i].getName();
                if (i != p.length - 1) param += ", ";
            }
            param += ")";
        }

        if (member instanceof Method) {
            param += "(";
            Method m = (Method) member;
            Parameter[] p = ((Method) member).getParameters();

            returnType = m.getReturnType().getName();

            for (int i = 0; i < p.length; i++) {
                param += p[i].getType().getName() + " " + p[i].getName();
                if (i != p.length - 1) param += ", ";
            }
            param += ")";
        }

        if (mod.equals("public")) {
            str = Modifier.toString(member.getModifiers()) + " " + returnType + " " + member.getName() + param + "\n";
        }

        if (mod.equals("private")) {
            str = Modifier.toString(member.getModifiers()) + " " + returnType + " " + member.getName() + param + "\n";
        }

        if (mod.equals("")) {
            str = Modifier.toString(member.getModifiers()) + " " + returnType + " " + member.getName() + param + "\n";
        }

        if (mod.equals("protected")) {
            str = Modifier.toString(member.getModifiers()) + " " + returnType + " " + member.getName() + param + "\n";
        }

        return str;
    }

    @PostMapping("/umlFromClasses")
    public String umlFormClasses(@ModelAttribute("classesForm") ClassesForm classesForm, Model model) throws ClassNotFoundException, MalformedURLException {
        //System.out.println("Folder: " + classesForm.getFolder().size());

        /*
        File dir = new File("C:/Users/Admin/Desktop/ZPO/projekt/TrinityProject/src/main/java/com/trinity/TrinityProject/exampleClasses");
        File[] listOfFiles = dir.listFiles();
        System.out.println(dir.getName());

         */

        /*
        List<Class<?>> listClasses = new ArrayList<>();
        listClasses.add(Class.forName("com.trinity.TrinityProject.exampleClasses.ExampleClass1"));

         */
        //System.out.println(clazz.getName());
        //System.out.println("field: " + clazz.getDeclaredFields()[0]);

        /*
        String[] classNames = new String[10];
        String[][] fields = new String[10][20];
        String[][] methods = new String[10][20];
        String[][] constructors = new String[10][20];

        int i = 0;
        for (Class<?> c : listClasses) {
            Class<?> clazz = c;
            String classStr = clazz.getSimpleName();
            classNames[i] = classStr;
            int fieldNum = 0;
            int methodNum = 0;
            int constrNum = 0;


            System.out.println(clazz.getSimpleName());

            for (Field field : clazz.getDeclaredFields()) {
                System.out.println(Modifier.toString(field.getModifiers()) + " " +field.getType() + " " + field.getName().replace("com.trinity.TrinityProject.exampleClasses.ExampleClass1.", " "));
                String fieldStr = Modifier.toString(field.getModifiers()) + " " +field.getType() + " " + field.getName().replace("com.trinity.TrinityProject.exampleClasses.ExampleClass1.", " ");
                fields[i][fieldNum] = fieldStr;
                fieldNum += 1;
            }

            for (Constructor constructor : clazz.getDeclaredConstructors()) {
                System.out.println(constructor.getName());
                String param = "(";

                Parameter[] p = constructor.getParameters();

                String returnType = constructor.getAnnotatedReturnType().toString();

                for (int k = 0; k < p.length; k++) {
                    param += p[k].getType().getName() + " " + p[k].getName();
                    if (k != p.length - 1) param += ", ";
                }
                param += ")";

                String constrStr = Modifier.toString(constructor.getModifiers()) + " " +
                        returnType.replace("com.trinity.TrinityProject.exampleClasses." , "") +
                        " " +
                        constructor.getName().replace("com.trinity.TrinityProject.exampleClasses.", "") +
                        param;
                constructors[i][constrNum] = constrStr;
                constrNum += 1;
            }

            for (Method method : clazz.getDeclaredMethods()) {
                System.out.println(method.getName());
                String param = "(";
                Parameter[] p = method.getParameters();

                String returnType = method.getReturnType().getName();

                for (int k = 0; k < p.length; k++) {
                    param += p[k].getType().getName() + " " + p[k].getName();
                    if (k != p.length - 1) param += ", ";
                }
                param += ")";

                String methodStr = Modifier.toString(method.getModifiers()) + " " + returnType + " " + method.getName() + param;
                methods[i][methodNum] = methodStr;
                methodNum += 1;

            }

            i += 1;
        }

        System.out.println();
        System.out.println("TEST");
        System.out.println("Fields");
        for (int k = 0; k < fields[0].length; k++) {
            if (fields[0][k] == null) break;
            System.out.println(fields[0][k]);
        }

        System.out.println();
        System.out.println("Constructors");
        for (int k = 0; k < constructors[0].length; k++) {
            if (constructors[0][k] == null) break;
            System.out.println(constructors[0][k]);
        }

        System.out.println();
        System.out.println("Methods");
        for (int k = 0; k < methods[0].length; k++) {
            if (methods[0][k] == null) break;
            System.out.println(methods[0][k]);
        }

         */


        /*
        for (File f : classesForm.getFolder()) {
            System.out.println("File name: " + f.getName());

            System.out.println("URL: " + f.toURL());
            //Class<?> clazz = Class.forName("file:/C:/Users/Admin/Desktop/ZPO/projekt/TrinityProject/ExampleClass1");


            //URL classUrl;
            //classUrl = new URL(f.toURL().toString());
            //URL[] classUrls = { classUrl };
            //URLClassLoader ucl = new URLClassLoader(classUrls);
            //Class clazz = ucl.loadClass(getClass(f));

            System.out.println(f.getName());
            System.out.println("Class name: " + clazz.getName());
            System.out.println("Fields: ");
            for (Field fld : clazz.getDeclaredFields()) {
                System.out.println(fld.getName());
            }
        }

         */

/*
        model.addAttribute("classNames", classNames);
        model.addAttribute("fields", fields);
        model.addAttribute("constructors", constructors);
        model.addAttribute("methods", methods);


 */
        //return "redirect:/showUml/" + classNames + "/" + fields + "/" + constructors + "/" + methods;
        return "redirect:/showUml";

    }

    @GetMapping("/showUml")
    public String showUML(
            //@PathVariable(value = "classNames") String[] classNames,
                          //@PathVariable(value = "fields") String[][] fields,
                          //@PathVariable(value = "constructors") String[][] constructors,
                          //@PathVariable(value = "methods") String[][] methods,
                          Model model) throws ClassNotFoundException {

        /*
        model.addAttribute("classNames", classNames);
        model.addAttribute("fields", fields);
        model.addAttribute("constructors", constructors);
        model.addAttribute("methods", methods);

        System.out.println("showUml");
        System.out.println(classNames[0]);

         */

        List<Class<?>> listClasses = new ArrayList<>();
        listClasses.add(Class.forName("com.trinity.TrinityProject.exampleClasses.ExampleClass1"));
        //System.out.println(clazz.getName());
        //System.out.println("field: " + clazz.getDeclaredFields()[0]);

        String[] classNames = new String[10];
        String[][] fields = new String[10][20];
        String[][] methods = new String[10][20];
        String[][] constructors = new String[10][20];

        int i = 0;
        for (Class<?> c : listClasses) {
            Class<?> clazz = c;
            String classStr = clazz.getSimpleName();
            classNames[i] = classStr;
            int fieldNum = 0;
            int methodNum = 0;
            int constrNum = 0;


            System.out.println(clazz.getSimpleName());

            for (Field field : clazz.getDeclaredFields()) {
                System.out.println(Modifier.toString(field.getModifiers()) + " " +field.getType() + " " + field.getName().replace("com.trinity.TrinityProject.exampleClasses.ExampleClass1.", " "));
                String fieldStr = Modifier.toString(field.getModifiers()) + " " +field.getType() + " " + field.getName().replace("com.trinity.TrinityProject.exampleClasses.ExampleClass1.", " ");
                fields[i][fieldNum] = fieldStr;
                fieldNum += 1;
            }

            for (Constructor constructor : clazz.getDeclaredConstructors()) {
                System.out.println(constructor.getName());
                String param = "(";

                Parameter[] p = constructor.getParameters();

                String returnType = constructor.getAnnotatedReturnType().toString();

                for (int k = 0; k < p.length; k++) {
                    param += p[k].getType().getName() + " " + p[k].getName();
                    if (k != p.length - 1) param += ", ";
                }
                param += ")";

                String constrStr = Modifier.toString(constructor.getModifiers()) + " " +
                        returnType.replace("com.trinity.TrinityProject.exampleClasses." , "") +
                        " " +
                        constructor.getName().replace("com.trinity.TrinityProject.exampleClasses.", "") +
                        param;
                constructors[i][constrNum] = constrStr;
                constrNum += 1;
            }

            for (Method method : clazz.getDeclaredMethods()) {
                System.out.println(method.getName());
                String param = "(";
                Parameter[] p = method.getParameters();

                String returnType = method.getReturnType().getName();

                for (int k = 0; k < p.length; k++) {
                    param += p[k].getType().getName() + " " + p[k].getName();
                    if (k != p.length - 1) param += ", ";
                }
                param += ")";

                String methodStr = Modifier.toString(method.getModifiers()) + " " + returnType + " " + method.getName() + param;
                methods[i][methodNum] = methodStr;
                methodNum += 1;

            }

            i += 1;
        }

        System.out.println();
        System.out.println("TEST");
        System.out.println("Fields");
        for (int k = 0; k < fields[0].length; k++) {
            if (fields[0][k] == null) break;
            System.out.println(fields[0][k]);
        }

        System.out.println();
        System.out.println("Constructors");
        for (int k = 0; k < constructors[0].length; k++) {
            if (constructors[0][k] == null) break;
            System.out.println(constructors[0][k]);
        }

        System.out.println();
        System.out.println("Methods");
        for (int k = 0; k < methods[0].length; k++) {
            if (methods[0][k] == null) break;
            System.out.println(methods[0][k]);
        }


        /*
        for (File f : classesForm.getFolder()) {
            System.out.println("File name: " + f.getName());

            System.out.println("URL: " + f.toURL());
            //Class<?> clazz = Class.forName("file:/C:/Users/Admin/Desktop/ZPO/projekt/TrinityProject/ExampleClass1");


            //URL classUrl;
            //classUrl = new URL(f.toURL().toString());
            //URL[] classUrls = { classUrl };
            //URLClassLoader ucl = new URLClassLoader(classUrls);
            //Class clazz = ucl.loadClass(getClass(f));

            System.out.println(f.getName());
            System.out.println("Class name: " + clazz.getName());
            System.out.println("Fields: ");
            for (Field fld : clazz.getDeclaredFields()) {
                System.out.println(fld.getName());
            }
        }

         */


        model.addAttribute("classNames", classNames);
        model.addAttribute("fields", fields);
        model.addAttribute("constructors", constructors);
        model.addAttribute("methods", methods);


        return "showUml";
    }
}
