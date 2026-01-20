package com.trinity.TrinityProject.exampleClasses;

public class ExampleClass1 {
    private int field1;
    private double field2;
    private String field3;

    public ExampleClass1 () {};

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
