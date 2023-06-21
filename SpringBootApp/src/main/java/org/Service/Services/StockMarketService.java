package org.Service.Services;

import org.DTOs.CustomerBalanceDTO;
import org.DTOs.StockMarketDTO;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.CustomerBalance;
import org.Service.Entities.StockMarket;
import org.Service.Entities.User;
import org.Service.Repositories.StockMarketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockMarketService {
    @Autowired
    private StockMarketRepo stockMarketRepo;

    @Autowired
    private UserService userService;

    public StockMarket addStockMarket(StockMarketDTO stockMarketDTO) {
        User user = userService.findUserById(stockMarketDTO.getUserId());
        StockMarket stockMarket = new StockMarket(user, stockMarketDTO.getUserId(), stockMarketDTO.getTicker(),
                stockMarketDTO.getQuantity(), stockMarketDTO.getTotallyBought());
        return stockMarketRepo.save(stockMarket);
    }

    public StockMarket findStockMarket(int id) {
        Optional<StockMarket> stockMarket = stockMarketRepo.findById(id);
        if (stockMarket.isPresent()) {
            return stockMarket.get();
        } else {
            throw new NoSuchElementException("customer balance not found");
        }
    }

    public List<StockMarket> findAllStocks(){
        return (List<StockMarket>) stockMarketRepo.findAll();
    }

    public StockMarket updateStockMarketById(StockMarketDTO stockMarketDTO) {
        User user = userService.findUserById(stockMarketDTO.getUserId());
        //StockMarket stockMarket = findStockMarket(stockMarketDTO.getStockId());
        StockMarket updatedStock = new StockMarket(user, stockMarketDTO.getStockId(), stockMarketDTO.getTicker(),
                stockMarketDTO.getQuantity(), stockMarketDTO.getTotallyBought());
        return stockMarketRepo.save(updatedStock);
    }

    public void deleteStockById(int stockId) {
        StockMarket stockMarket = findStockMarket(stockId);
        stockMarketRepo.delete(stockMarket);
    }

    public List<StockMarket> findAllStocksForOneClient(int userId) {
        List<StockMarket> allStocks = (List<StockMarket>) stockMarketRepo.findAll();
        List<StockMarket> stockMarketsForOneClient = new ArrayList<>();
        for (StockMarket stockMarket : allStocks) {
            if(stockMarket.getUser().getUserId()==userId){
                stockMarketsForOneClient.add(stockMarket);
            }
        }
        return stockMarketsForOneClient;
    }

    public boolean isStockInPortfolio(String ticker, int userId){
        List<StockMarket> allStocks = (List<StockMarket>) stockMarketRepo.findAll();
        for(StockMarket stockMarket: allStocks){
            if (stockMarket.getTicker().equals(ticker)&&stockMarket.getUser().getUserId()==userId){
                return true;
            }
        }
        return false;

    }
}
