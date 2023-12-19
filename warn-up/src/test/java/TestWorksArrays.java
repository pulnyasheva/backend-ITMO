import org.hw1.WorkArrays;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestWorksArrays {

    @Test
    public void test01_squareAndAddTenEmpty() {
        assertEquals(Collections.emptyList(),
                WorkArrays.squareAndAddTenWithout56End(Collections.emptyList()));
    }

    @Test
    public void test02_squareAndAddTenOneElementWith56() {
        assertEquals(Collections.emptyList(),
                WorkArrays.squareAndAddTenWithout56End(List.of(5)));
    }

    @Test
    public void test03_squareAndAddTenOneElement() {
        assertEquals(Collections.emptyList(),
                WorkArrays.squareAndAddTenWithout56End(List.of(4)));
    }

    @Test
    public void test04_squareAndAddTenElements() {
        assertEquals(List.of(11, 14, 19, 59, 74, 91),
                WorkArrays.squareAndAddTenWithout56End(List.of(1, 2, 3, 7, 8, 9)));
    }

    @Test
    public void test05_squareAndAddTenElementsWith56() {
        assertEquals(List.of(11, 14, 19, 59, 74, 91),
                WorkArrays.squareAndAddTenWithout56End(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void test06_countDuplicatesEmpty() {
        assertEquals(Collections.emptyMap(),
                WorkArrays.countDuplicates(Collections.emptyList()));
    }

    @Test
    public void test06_countDuplicatesOneElement() {
        assertEquals(Collections.emptyMap(),
                WorkArrays.countDuplicates(List.of(1)));
    }

    @Test
    public void test07_countDuplicatesOneDuplicate() {
        assertEquals(Map.of(1, 3L),
                WorkArrays.countDuplicates(List.of(1, 1, 1)));
    }

    @Test
    public void test08_countDuplicatesElements() {
        assertEquals(Collections.emptyMap(),
                WorkArrays.countDuplicates(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void test09_countDuplicatesManyDuplicate() {
        assertEquals(Map.of(1, 3L, 4, 2L, 7, 4L),
                WorkArrays.countDuplicates(
                        List.of(1, 1, 1, 2, 3, 4, 4, 5, 6, 7, 7, 7, 7, 8, 9)));
    }
}
