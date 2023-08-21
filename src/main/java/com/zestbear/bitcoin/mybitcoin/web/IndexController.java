package com.zestbear.bitcoin.mybitcoin.web;

import com.zestbear.bitcoin.mybitcoin.service.CandleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    private final CandleService candleService;

    public IndexController(CandleService candleService) {
        this.candleService = candleService;
    }

    @GetMapping("/")
    public String showCandleData(Model model) {
        String[] coinSymbols = {"BTC", "ETH", "ETC", "ADA"};
        List<String> candleDataList = new ArrayList<>();
        for (String coinSymbol : coinSymbols) {
            String imgUrl = "https://example.com/path/to/" + coinSymbol + "_candle_chart.png";
            System.out.println("Image URL: " + imgUrl);  // 로그 출력
            candleDataList.add(imgUrl);
        }
        model.addAttribute("candleDataList", candleDataList);
        return "index";
    }

}
