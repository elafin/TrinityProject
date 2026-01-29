package com.trinity.TrinityProject.controller;

import com.trinity.TrinityProject.annotations.CustomColor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LocalReflectionController {

    @GetMapping("/showClassDiagramFromLocalDir")
    public String showUML(Model model) throws ClassNotFoundException {

        List<Class<?>> listClasses = new ArrayList<>();
        listClasses.add(Class.forName("com.trinity.TrinityProject.exampleClasses.ExampleClass1"));
        listClasses.add(Class.forName("com.trinity.TrinityProject.exampleClasses.ExampleClass2"));

        String[] classNames = new String[10];
        String[][] fields = new String[10][20];
        String[][] methods = new String[10][20];
        String[][] constructors = new String[10][20];
        int[][] rgb = new int[10][3];

        int i = 0;
        for (Class<?> clazz : listClasses) {
            //Class<?> clazz = c;
            String classStr = clazz.getSimpleName();
            classNames[i] = classStr;
            int fieldNum = 0;
            int methodNum = 0;
            int constrNum = 0;

            if (!(clazz.isAnnotationPresent(CustomColor.class))) {

                CustomColor[] colors = clazz.getAnnotationsByType(CustomColor.class);

                int r = 0;
                int g = 0;
                int b = 0;

                for (CustomColor col : colors) {
                    r = r + col.rgb()[0];
                    g = g + col.rgb()[1];
                    b = b + col.rgb()[2];
                }

                rgb[i][0] = r;
                rgb[i][1] = g;
                rgb[i][2] = b;

            }

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

        model.addAttribute("classNames", classNames);
        model.addAttribute("fields", fields);
        model.addAttribute("constructors", constructors);
        model.addAttribute("methods", methods);
        model.addAttribute("rgb", rgb);


        return "showUml";
    }
}
