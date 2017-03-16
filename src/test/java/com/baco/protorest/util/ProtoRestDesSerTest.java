package com.baco.protorest.util;

import io.protostuff.CodedInput;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by deiby.nahuat on 16/03/2017.
 */
public class ProtoRestDesSerTest {

    public class PojoTest {
        private Integer value1;
        private Integer value2;
        private String value3;

        public PojoTest(Integer value1, Integer value2, String value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        public PojoTest() {

        }

        public Integer getValue1() {
            return value1;
        }

        public void setValue1(Integer value1) {
            this.value1 = value1;
        }

        public Integer getValue2() {
            return value2;
        }

        public void setValue2(Integer value2) {
            this.value2 = value2;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PojoTest pojoTest = (PojoTest) o;

            if (value1 != null ? !value1.equals(pojoTest.value1) : pojoTest.value1 != null) return false;
            if (value2 != null ? !value2.equals(pojoTest.value2) : pojoTest.value2 != null) return false;
            return value3 != null ? value3.equals(pojoTest.value3) : pojoTest.value3 == null;

        }

        @Override
        public int hashCode() {
            int result = value1 != null ? value1.hashCode() : 0;
            result = 31 * result + (value2 != null ? value2.hashCode() : 0);
            result = 31 * result + (value3 != null ? value3.hashCode() : 0);
            return result;
        }
    }

    @Test
    public void writeReadPojoTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PojoTest pt = new PojoTest();
        ProtoRestDesSer.writeProtorest(pt, PojoTest.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PojoTest pt2 = (PojoTest) ProtoRestDesSer.readProtorest(PojoTest.class, bis);

        assertEquals(pt, pt2);
    }

    @Test
    public void writeReadDateTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Date date = Calendar.getInstance().getTime();
        ProtoRestDesSer.writeProtorest(date, Date.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Date date2 = (Date) ProtoRestDesSer.readProtorest(Date.class, bis);
        assertEquals(date, date2);
    }

    @Test
    public void writeReadIntegerTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Integer a = 100;

        ProtoRestDesSer.writeProtorest(a, Integer.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Integer a2 = (Integer) ProtoRestDesSer.readProtorest(Integer.class, bis);
        assertEquals(a, a2);

    }

    @Test
    public void writeReadStringTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String a = "100";

        ProtoRestDesSer.writeProtorest(a, String.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        String a2 = (String) ProtoRestDesSer.readProtorest(String.class, bis);
        assertEquals(a, a2);
    }

    @Test
    public void writeReadPojoListSizeTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<PojoTest> pojoList = new ArrayList();

        for(int i = 0; i < 10; ++i) {
            pojoList.add(new PojoTest(i,i, String.valueOf(i)));
        }

        ProtoRestDesSer.writeProtorest(pojoList, List.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        List<PojoTest> pojoList2 = (List<PojoTest>) ProtoRestDesSer.readProtorest(List.class, bis);

        assertEquals(pojoList.size(), pojoList2.size());

    }

    @Test
    public void writeReadPojoListCompleteTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<PojoTest> pojoList = new ArrayList();

        for(int i = 0; i < 10; ++i) {
            pojoList.add(new PojoTest(i,i, String.valueOf(i)));
        }

        ProtoRestDesSer.writeProtorest(pojoList, List.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        List<PojoTest> pojoList2 = (List<PojoTest>) ProtoRestDesSer.readProtorest(List.class, bis);

        for(PojoTest pojoTest2: pojoList2) {
            assertTrue(pojoList.contains(pojoTest2));
        }

    }

    @Test
    public void writeReadHugePojoListSizeTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<PojoTest> pojoList = new ArrayList();

        for(int i = 0; i < 100000; ++i) {
            pojoList.add(new PojoTest(i,i, String.valueOf(i)));
        }
        ProtoRestDesSer.writeProtorest(pojoList, List.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        List<PojoTest> pojoList2 = (List<PojoTest>) ProtoRestDesSer.readProtorest(List.class, bis);

        assertEquals(pojoList.size(), pojoList2.size());

    }

    @Test
    public void writeReadHugePojoListCompleteTest() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<PojoTest> pojoList = new ArrayList();

        for(int i = 0; i < 100000; ++i) {
            pojoList.add(new PojoTest(i,i, String.valueOf(i)));
        }

        ProtoRestDesSer.writeProtorest(pojoList, List.class, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        List<PojoTest> pojoList2 = (List<PojoTest>) ProtoRestDesSer.readProtorest(List.class, bis);

        for(PojoTest pojoTest2: pojoList2) {
            assertTrue(pojoList.contains(pojoTest2));
        }

    }


}