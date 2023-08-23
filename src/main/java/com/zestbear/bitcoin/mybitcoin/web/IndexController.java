package com.zestbear.bitcoin.mybitcoin.web;

import com.zestbear.bitcoin.mybitcoin.domain.Candle.CandleData;
import com.zestbear.bitcoin.mybitcoin.service.CandleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final CandleService candleService;

    @GetMapping("/")
    public String Index(Model model) {
        String[] coinSymbols = {"BTC", "ETH", "ETC", "ADA"};
        Map<String, List<CandleData>> jsonMap = candleService.getCandleData(coinSymbols);

        model.addAttribute("jsonMap", jsonMap);
        return "index";
    }
}
