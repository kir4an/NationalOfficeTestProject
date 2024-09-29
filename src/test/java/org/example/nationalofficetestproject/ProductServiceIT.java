package org.example.nationalofficetestproject;

import org.example.nationalofficetestproject.dto.ProductDto;
import org.example.nationalofficetestproject.repository.ProductRepository;
import org.example.nationalofficetestproject.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductServiceIT extends AbstractTestContainers {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSelect1MillionProductsWithMultipleConnections() throws InterruptedException {
        createMillionProducts();

        int numThreads = 100;
        int totalRequests = 1_000_000;
        int requestsPerThread = totalRequests / numThreads;
        ExecutorService executor2 = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch2 = new CountDownLatch(numThreads);
        ArrayBlockingQueue<Long> executionTimes = new ArrayBlockingQueue<>(totalRequests);

        for (int i = 0; i < numThreads; i++) {
            executor2.execute(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        long randomId = new Random().nextInt(1_000_000) + 1;

                        long startTime = System.currentTimeMillis();
                        productService.getById(randomId);
                        long endTime = System.currentTimeMillis();

                        executionTimes.add(endTime - startTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch2.countDown();
                }
            });
        }

        latch2.await(10, TimeUnit.MINUTES);
        executor2.shutdown();

        if (!executor2.awaitTermination(5, TimeUnit.MINUTES)) {
            System.err.println("Not all threads finished in time.");
        } else {
            System.out.println("All threads completed successfully.");

        }

        System.out.println("Total execution times: " + executionTimes.size());
        assertEquals(totalRequests, executionTimes.size());

        List<Long> timesList = new ArrayList<>();
        executionTimes.drainTo(timesList);

        Collections.sort(timesList);

        //Медианное - среднее двух центральных элементов
        double median = (timesList.get(timesList.size() / 2 - 1) + timesList.get(timesList.size() / 2)) / 2.0;

        double percentile95 = timesList.get((int) (0.95 * timesList.size()));
        double percentile99 = timesList.get((int) (0.99 * timesList.size()));

        double mean = timesList.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);

        System.out.printf("Mean execution time: %.2f ms%n", mean);
        System.out.printf("Median execution time: %.2f ms%n", median);
        System.out.printf("95th Percentile execution time: %.2f ms%n", percentile95);
        System.out.printf("99th Percentile execution time: %.2f ms%n", percentile99);
    }

    @Test
    void testCreate100000Products() throws InterruptedException {
        int numThreads = 100;
        int productsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < productsPerThread; j++) {
                        ProductDto productDto = ProductDto.builder()
                                .name("Product " + j)
                                .description("Description for Product " + j)
                                .price(new Random().nextDouble() * 1000)
                                .build();
                        productService.saveProduct(productDto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(100000, productRepository.count());
    }

    void createMillionProducts() throws InterruptedException {
        int numThreads = 100;
        int productsPerThread = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < productsPerThread; j++) {
                        ProductDto productDto = ProductDto.builder()
                                .name("Product " + j)
                                .description("Description for Product " + j)
                                .price(new Random().nextDouble() * 1000)
                                .build();
                        productService.saveProduct(productDto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

    }
}