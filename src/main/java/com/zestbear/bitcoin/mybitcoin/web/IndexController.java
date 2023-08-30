package com.zestbear.bitcoin.mybitcoin.web;

import com.zestbear.bitcoin.mybitcoin.service.Execute.ExecuteService;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.APIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ExecuteService executeService;

    @GetMapping("/")
    public String Index() throws IOException, NoSuchAlgorithmException, ExecutionException, InterruptedException {

        executeService.execute();

        return "index";
    }
}
