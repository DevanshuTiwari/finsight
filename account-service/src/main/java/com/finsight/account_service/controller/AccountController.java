package com.finsight.account_service.controller;


import com.finsight.account_service.dto.UploadResponse;
import com.finsight.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/accounts/transactions")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/upload-csv")
    public UploadResponse importTransactionsFromCsv(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("userId") Long userId) {
        return accountService.importTransactionsFromCsv(file, userId);
    }
}
