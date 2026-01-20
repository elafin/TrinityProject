package com.trinity.TrinityProject.model;

import java.io.File;
import java.util.List;

public class ClassesForm {

    private List<File> folder;

    public ClassesForm () {}

    public ClassesForm(List<File> folder) {
        this.folder = folder;
    }

    public List<File> getFolder() {
        return folder;
    }

    public void setFolder(List<File> folder) {
        this.folder = folder;
    }
}
