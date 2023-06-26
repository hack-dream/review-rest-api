package com.review.app.rest;

import com.review.app.rest.Controller.ApiControllers;
import com.review.app.rest.Models.Product;
import com.review.app.rest.Models.Review;
import com.review.app.rest.Repo.ProductRepo;
import com.review.app.rest.Repo.ReviewRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestapiApplicationTest {
    @Mock
    private ProductRepo productRepo;

    @Mock
    private ReviewRepo reviewRepo;

    @InjectMocks
    ApiControllers controller = new ApiControllers();

    @Test
    public void testCreateProduct() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Product product = new Product();
        product.setId(1);

        when(productRepo.save(product)).thenReturn(product);

        ResponseEntity<Product> responseEntity = controller.createProduct(product);

        assert (responseEntity.getStatusCode() == HttpStatusCode.valueOf(201));
        assert (Objects.equals(Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath(), "/product/1"));
    }

    @Test
    public void testBadCreateReview() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Review review = new Review();
        review.setId(1);

        ResponseEntity<Object> responseEntity = controller.createReview(1, review);

        assert (responseEntity.getStatusCode() == HttpStatusCode.valueOf(400));
    }

    @Test
    public void testGoodCreateReview() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Product product = new Product();
        product.setId(1);

        when(productRepo.save(product)).thenReturn(product);
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        Review review = new Review();

        ResponseEntity<Object> responseEntity = controller.createReview(1, review);

        assert (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200));
    }

    @Test
    public void testDeleteProduct() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(productRepo.existsById(1L)).thenReturn(true);
        ResponseEntity<Object> goodResponseEntity = controller.deleteProduct(1);

        when(productRepo.existsById(2L)).thenReturn(false);
        ResponseEntity<Object> badResponseEntity = controller.deleteProduct(2);

        assert (goodResponseEntity.getStatusCode() == HttpStatusCode.valueOf(200));
        assert (badResponseEntity.getStatusCode() == HttpStatusCode.valueOf(404));
    }

    @Test
    public void CalculateRating() {
        Product product = new Product();
        Review review1 = new Review();
        review1.setRating(4);
        Review review2 = new Review();
        review2.setRating(3);

        product.addReview(review1);
        product.addReview(review2);

        assert (product.getRating() == 3.5);
    }
}
