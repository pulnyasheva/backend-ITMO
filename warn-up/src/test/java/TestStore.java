import org.hw1.Store;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestStore {

    private static final Store store = new Store();
    private static final String PRODUCT = "product";

    private static final int COUNT_PRODUCTS = 10;

    @Test
    public void test01_addAndGetOneProducts() {
        store.addProducts(PRODUCT);
        assertEquals(PRODUCT,
                store.getProduct());
    }

    @Test
    public void test02_addAndGetManyProducts() {
        Set<String> products = new HashSet<>();
        addProducts(false, products);
        for (int i = 1; i <= COUNT_PRODUCTS; i++) {
            getProducts(products);
        }
    }

    @Test
    public void test03_addAndGetManyProductsParallel() {
        Set<String> products = new HashSet<>();
        Thread supplier = new Thread(() -> addProducts(true, products));
        supplier.start();

        for (int i = 1; i <= 10; i++) {
            Thread consumer = new Thread(() -> getProducts(products));
            consumer.start();
        }
    }

    private void addProducts(boolean isThread, Set<String> products) {
        for (int i = 1; i <= COUNT_PRODUCTS; i++) {
            String product = PRODUCT + i;
            store.addProducts(product);
            products.add(product);
            if (isThread) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getProducts(Set<String> products) {
        String product = store.getProduct();
        assert (products.contains(product));
        products.remove(product);
    }

}
