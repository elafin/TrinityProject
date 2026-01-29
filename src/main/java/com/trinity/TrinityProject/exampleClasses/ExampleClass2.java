package com.trinity.TrinityProject.exampleClasses;

import com.trinity.TrinityProject.annotations.CustomColor;
import com.trinity.TrinityProject.annotations.CustomColorRepeatable;

@CustomColor(rgb = {0, 255, 0})
@CustomColor(rgb = {0, 0, 255})
public class ExampleClass2<G, K> {
    private G field1;
    protected K field2;

    public ExampleClass2(G g, K k) {
        this.field1 = g;
        this.field2 = k;
    }

    public G getField1() {
        return field1;
    }

    public void setField1(G field1) {
        this.field1 = field1;
    }

    public K getField2() {
        return field2;
    }

    public void setField2(K field2) {
        this.field2 = field2;
    }
}
