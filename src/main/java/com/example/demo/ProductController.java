package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/list";
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/create";
    }

    @PostMapping("/create")
    public String createProduct(
        @ModelAttribute Product product,
        @RequestParam("file") MultipartFile file
    ) throws IOException{
        if (!file.isEmpty()) {
            product.setImage(file.getBytes());
        }
        productService.save(product);
        return "redirect:/products";
    }
    
    
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    } 


    @GetMapping("/edit/{id}")
    public String editProductForm(
        @PathVariable Long id, Model model
    ) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/edit";
    }

    @PostMapping("/edit/{id}")
    public String EditProduct(
        @PathVariable Long id,
        @ModelAttribute Product product,
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        Product existingProduct = productService.findById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQty(product.getQty());
        existingProduct.setCategory(product.getCategory());

        if (!file.isEmpty()) {
            existingProduct.setImage(file.getBytes());
        }

        productService.save(existingProduct);
        return "redirect:/products";

    }


    @GetMapping("/image/{id}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        Product product = productService.findById(id);
        return product.getImage();
    }


}
