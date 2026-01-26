package com.trinity.TrinityProject.controller;

import com.trinity.TrinityProject.model.ClassesForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"classNames", "fields", "constructors", "methods"})
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("home1", "Witaj na stronie domowej!");
        model.addAttribute("home2", "Znajdziesz tu info o stronie.");


        ClassesForm classesForm = new ClassesForm(new ArrayList<>());
        model.addAttribute("classesForm", classesForm);

        return "home";
    }



    private static String formatParams(Parameter[] p) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < p.length; i++) {
            sb.append(p[i].getType().getName()).append(" ").append(p[i].getName());
            if (i != p.length - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    private static String fieldToString(Field field) {
        String mod = Modifier.toString(field.getModifiers());
        return mod + " " + field.getType().getName() + " " + field.getName();
    }

    private static String constructorToString(Constructor<?> constructor) {
        String mod = Modifier.toString(constructor.getModifiers());
        String returnType = constructor.getAnnotatedReturnType().toString();
        String params = formatParams(constructor.getParameters());


        String ctorName = constructor.getDeclaringClass().getSimpleName();

        return mod + " " + returnType.replace("class ", "") + " " + ctorName + params;
    }

    private static String methodToString(Method method) {
        String mod = Modifier.toString(method.getModifiers());
        String returnType = method.getReturnType().getName();
        String params = formatParams(method.getParameters());
        return mod + " " + returnType + " " + method.getName() + params;
    }


    @PostMapping("/umlFromJar")
    public String umlFromJar(
            @RequestParam("jarFile") MultipartFile jarFile,
            @RequestParam(name = "publicOnly", defaultValue = "false") boolean publicOnly,
            Model model
    ) throws Exception {

        if (jarFile.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano pliku.");
        }
        String original = jarFile.getOriginalFilename() == null ? "" : jarFile.getOriginalFilename().toLowerCase();
        if (!original.endsWith(".jar")) {
            throw new IllegalArgumentException("To nie jest plik .jar");
        }

        // 1) zapis do temp
        Path tmpJar = Files.createTempFile("uml-", ".jar");
        jarFile.transferTo(tmpJar.toFile());

        // 2) odczyt klas z jar
        List<String> fqcnList;
        try (JarFile jar = new JarFile(tmpJar.toFile())) {
            fqcnList = jar.stream()
                    .map(JarEntry::getName)
                    .filter(n -> n.endsWith(".class"))
                    .filter(n -> !n.contains("$")) // na start pomijamy klasy wewnętrzne
                    // OBSŁUGA: zwykły jar -> com/x/Y.class
                    // jeśli masz Spring Boot jar -> BOOT-INF/classes/com/x/Y.class
                    .map(n -> n.startsWith("BOOT-INF/classes/")
                            ? n.substring("BOOT-INF/classes/".length())
                            : n)
                    .map(n -> n.replace("/", ".").replace(".class", ""))
                    .collect(Collectors.toList());
        }

        // 3) classloader do tego jara
        URL[] urls = { tmpJar.toUri().toURL() };
        List<Class<?>> loaded = new ArrayList<>();

        try (URLClassLoader cl = new URLClassLoader(urls, this.getClass().getClassLoader())) {
            for (String fqcn : fqcnList) {
                try {
                    Class<?> clazz = Class.forName(fqcn, false, cl);
                    loaded.add(clazz);
                } catch (Throwable ex) {

                }
            }
        } finally {
            Files.deleteIfExists(tmpJar);
        }


        int classCount = loaded.size();

        String[] classNames = new String[classCount];

        int perClassLimit = 200;
        String[][] fields = new String[classCount][perClassLimit];
        String[][] constructors = new String[classCount][perClassLimit];
        String[][] methods = new String[classCount][perClassLimit];

        for (int i = 0; i < classCount; i++) {
            Class<?> clazz = loaded.get(i);
            classNames[i] = clazz.getSimpleName();

            int fIdx = 0;
            for (Field f : clazz.getDeclaredFields()) {
                if (publicOnly && !Modifier.isPublic(f.getModifiers())) continue;
                if (fIdx >= perClassLimit) break;
                fields[i][fIdx++] = fieldToString(f);
            }

            int cIdx = 0;
            for (Constructor<?> c : clazz.getDeclaredConstructors()) {
                if (publicOnly && !Modifier.isPublic(c.getModifiers())) continue;
                if (cIdx >= perClassLimit) break;
                constructors[i][cIdx++] = constructorToString(c);
            }

            int mIdx = 0;
            for (Method m : clazz.getDeclaredMethods()) {
                if (publicOnly && !Modifier.isPublic(m.getModifiers())) continue;
                if (mIdx >= perClassLimit) break;
                methods[i][mIdx++] = methodToString(m);
            }
        }


        model.addAttribute("classNames", classNames);
        model.addAttribute("fields", fields);
        model.addAttribute("constructors", constructors);
        model.addAttribute("methods", methods);

        return "redirect:/showUml";
    }

    @GetMapping("/showUml")
    public String showUML(Model model) {

        if (!model.containsAttribute("classNames")) {
            model.addAttribute("classNames", new String[0]);
            model.addAttribute("fields", new String[0][0]);
            model.addAttribute("constructors", new String[0][0]);
            model.addAttribute("methods", new String[0][0]);
        }
        return "showUml";
    }


    @PostMapping("/clearUml")
    public String clearUml(SessionStatus status) {
        status.setComplete();
        return "redirect:/";
    }
}
