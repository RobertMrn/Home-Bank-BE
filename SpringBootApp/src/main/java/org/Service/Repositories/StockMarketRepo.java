package org.Service.Repositories;

import org.Service.Entities.StockMarket;
import org.Service.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface StockMarketRepo extends CrudRepository<StockMarket, Integer> {

    public StockMarket findStockMarketByUser(User user);
}
