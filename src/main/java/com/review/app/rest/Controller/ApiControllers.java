package com.review.app.rest.Controller;

import com.review.app.rest.Models.Product;
import com.review.app.rest.Models.Review;
import com.review.app.rest.Repo.ProductRepo;
import com.review.app.rest.Repo.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@RestController
public class ApiControllers {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @PostMapping(value = "/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productRepo.save(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/product/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/product/{product_id}")
    public ResponseEntity<Object> getProduct(@PathVariable long product_id) {
        Optional<Product> product = productRepo.findById(product_id);

        if (product.isPresent()) {
            return ResponseEntity.ok().body(product.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/product/{product_id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long product_id) {
        if (productRepo.existsById(product_id)) {
            productRepo.deleteById(product_id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/product/{product_id}/review")
    public ResponseEntity<Object> createReview(@PathVariable long product_id, @RequestBody Review review) {
        Optional<Product> product = productRepo.findById(product_id);
        if (product.isPresent()) {
            Product product_value = product.get();
            product_value.addReview(review);
            productRepo.save(product_value);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
