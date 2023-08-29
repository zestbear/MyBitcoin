package com.zestbear.bitcoin.mybitcoin.web;

import com.zestbear.bitcoin.mybitcoin.service.Account.AccountService;
import com.zestbear.bitcoin.mybitcoin.service.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.Candle.CurrentValueService;
import com.zestbear.bitcoin.mybitcoin.service.Candle.DayMovingAverageLineService;
import com.zestbear.bitcoin.mybitcoin.service.Order.Execute;
import com.zestbear.bitcoin.mybitcoin.service.Order.OrderService;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.MovingAverageLineService;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.ProportionService;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final Execute execute;

    @GetMapping("/")
    public String Index() throws IOException, NoSuchAlgorithmException {

        execute.execute();

        return "index";
    }
}
