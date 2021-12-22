package com.peter.springboot.store.service;

import com.peter.springboot.store.dao.CustomerRepository;
import com.peter.springboot.store.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    // 1 constructor nên ko cần dùng @Autowired
    public CustomerServiceImpl(CustomerRepository r) {
        repo = r;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    @Override
    public List<Customer> getAllCustomersActive() {
        return repo.findByStatusTrue();
    }

    @Override
    public List<Customer> findByNameContaining(String searchValue) {
        return repo.findByNameContaining(searchValue);
    }

    @Override
    public Customer getCustomer(int id) {
        Optional<Customer> o = repo.findById(id);
        if (o.isPresent()) {
            return o.get();
        } else
            return null;
    }

    @Override
    public void saveCustomer(Customer c) {
        repo.save(c);
    }

    @Override
    public void deleteCustomer(int id) {
        Customer c = repo.getById(id);
        c.setStatus(false);
        repo.save(c);
    }

    @Override
    public Customer checkLogin(String email, String password) {
        Customer c = null;
        List<Customer> list = repo.findAll();
        for (Customer tmp : list) {
            if (tmp.getEmail().equals(email) && tmp.getPassword().equals(password)) {
                c = tmp;
            }
        }
        return c;
    }
}
