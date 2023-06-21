package org.Service.Controllers;

import org.DTOs.CustomerBalanceDTO;
import org.DTOs.StockMarketDTO;
import org.Service.Entities.CustomerBalance;
import org.Service.Entities.StockMarket;
import org.Service.Repositories.CustomerBalanceRepo;
import org.Service.Services.CustomerBalanceService;
import org.Service.Services.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin
public class StockController {
    @Autowired
    private StockMarketService stockMarketService;

    @Autowired
    private CustomerBalanceService customerBalanceService;

    @Autowired
    private CustomerBalanceRepo customerBalanceRepo;

    @PostMapping("/buyStockMarket")
    public void addStockMarket(@RequestBody StockMarketDTO stockMarketDTO) {
        boolean isStockAlreadyBought = false;
        List<StockMarket> stockMarkets = stockMarketService.findAllStocks();
        for (StockMarket stockMarket : stockMarkets) {
            if (stockMarket.getUser().getUserId() == stockMarketDTO.getUserId() && stockMarket.getTicker().equals(stockMarketDTO.getTicker())) {
                stockMarketDTO.setQuantity(stockMarketDTO.getQuantity() + stockMarket.getQuantity());
                stockMarketDTO.setTotallyBought(stockMarketDTO.getTotallyBought().add(stockMarket.getTotallyBought()));
                stockMarketDTO.setStockId(stockMarket.getStockId());
                stockMarketService.updateStockMarketById(stockMarketDTO);
                isStockAlreadyBought = true;
                StockMarket stockMarket1 = stockMarketService.findStockMarket(stockMarket.getStockId());
                CustomerBalance customerBalance = customerBalanceRepo.findCustomerBalanceByUser(stockMarket1.getUser());
                CustomerBalanceDTO customerBalanceDTO = new CustomerBalanceDTO(stockMarket1.getUser().getUserId(),
                        customerBalance.getBalance().subtract(stockMarket1.getTotallyBought()));
                customerBalanceService.updateCustomerBalanceById(customerBalanceDTO);
                break;
            }
        }
        if (!isStockAlreadyBought) {
            StockMarket stockMarket = stockMarketService.addStockMarket(stockMarketDTO);
            CustomerBalance customerBalance = customerBalanceRepo.findCustomerBalanceByUser(stockMarket.getUser());
            CustomerBalanceDTO customerBalanceDTO = new CustomerBalanceDTO(stockMarket.getUser().getUserId(),
                    customerBalance.getBalance().subtract(stockMarket.getTotallyBought()));
            customerBalanceService.updateCustomerBalanceById(customerBalanceDTO);
        }
    }

    @PostMapping("/sellStockMarket")
    public void sellStockMarket(@RequestBody StockMarketDTO stockMarketDTO) {
        List<StockMarket> stockMarkets = stockMarketService.findAllStocks();
        for (StockMarket stockMarket : stockMarkets) {
            if (stockMarket.getUser().getUserId() == stockMarketDTO.getUserId() && stockMarket.getTicker().equals(stockMarketDTO.getTicker())) {
                stockMarketDTO.setQuantity(stockMarket.getQuantity() - stockMarketDTO.getQuantity());
                stockMarketDTO.setTotallyBought(stockMarket.getTotallyBought().subtract(stockMarketDTO.getTotallyBought()));
                stockMarketDTO.setStockId(stockMarket.getStockId());
                if (stockMarketDTO.getQuantity() == 0 && stockMarketDTO.getTotallyBought().compareTo(BigDecimal.ZERO) == 0) {
                    stockMarketService.deleteStockById(stockMarket.getStockId());
                } else {
                    stockMarketService.updateStockMarketById(stockMarketDTO);
                }
                break;
            }
        }

    }

    @GetMapping("/findStockMarket")
    public StockMarket findStockMarket(@RequestParam int stockId) {
        return stockMarketService.findStockMarket(stockId);
    }

    @PutMapping("/updateStockMarketById")
    public StockMarket updateStockMarketById(@RequestBody StockMarketDTO stockMarketDTO) {
        return stockMarketService.updateStockMarketById(stockMarketDTO);
    }

    @DeleteMapping("/deleteStockById")
    public void deleteStockById(@RequestParam int stockId) {
        StockMarket stockMarket = stockMarketService.findStockMarket(stockId);
        CustomerBalance customerBalance = customerBalanceRepo.findCustomerBalanceByUser(stockMarket.getUser());
        CustomerBalanceDTO customerBalanceDTO = new CustomerBalanceDTO(stockMarket.getUser().getUserId(),
                stockMarket.getTotallyBought().add(customerBalance.getBalance()));
        customerBalanceService.updateCustomerBalanceById(customerBalanceDTO);
        stockMarketService.deleteStockById(stockId);
    }

    @PostMapping("/addCustomerBalance")
    public CustomerBalance addCustomerBalance(@RequestBody CustomerBalanceDTO customerBalanceDTO){
        return customerBalanceService.addCustomerBalance(customerBalanceDTO);
    }

    @GetMapping("/getBalance")
    public CustomerBalance findCustomerBalance(@RequestParam int userId) {
        return customerBalanceService.findCustomerBalance(userId);
    }

    @GetMapping("/getOnlyBalance")
    public BigDecimal findOnlyCustomerBalance(@RequestParam int userId) {
        CustomerBalance customerBalance = customerBalanceService.findCustomerBalance(userId);
        return customerBalance.getBalance();
    }

    @GetMapping("/findAllStocksForOneClient")
    public List<StockMarket> findAllStocksForOneClient(@RequestParam int userId) {
        return stockMarketService.findAllStocksForOneClient(userId);
    }

    @GetMapping("/isStockInPortfolio")
    public boolean isStockInPortfolio(@RequestParam String ticker, @RequestParam int userId) {
        return stockMarketService.isStockInPortfolio(ticker, userId);
    }
}
