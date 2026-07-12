package com.bank.bankingbackend.service;

import com.bank.bankingbackend.entity.Customer;
import java.util.List;

public interface CustomerService {

    Customer getAccount(Long accno);

}