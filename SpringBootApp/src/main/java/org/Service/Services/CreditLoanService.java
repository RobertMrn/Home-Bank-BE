package org.Service.Services;

import org.DTOs.CreditLoanDTO;
import org.DTOs.CreditLoansForUser;
import org.DTOs.UserDataAndConsumerData;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.CreditLoanConsumerData;
import org.Service.Entities.User;
import org.Service.Repositories.CreditLoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CreditLoanService {
    @Autowired
    private CreditLoanRepo creditLoanRepo;

    @Autowired
    private UserService userService;
    public CreditLoan addCreditLoan(CreditLoanDTO creditLoan) {
        User user = userService.findUserById(creditLoan.getUser());
        return creditLoanRepo.save(new CreditLoan(creditLoan.getContractId(), user, creditLoan.getAmount(), creditLoan.getInterestRate(),
                creditLoan.getTenure(), creditLoan.getInstallment(), creditLoan.getAmountToBePaid(), creditLoan.getCreditBureauScore(),
                creditLoan.getEsDecision(), creditLoan.getCreditType(), creditLoan.getCreationDate(), creditLoan.getLastUpdate()));
    }

    public CreditLoan findCreditLoanById(int id) {
        Optional<CreditLoan> creditLoan = creditLoanRepo.findById(id);
        if (creditLoan.isPresent()) {
            return creditLoan.get();
        } else {
            throw new NoSuchElementException("credit loan not found");
        }
    }

    public List<CreditLoansForUser> findCreditLoansByUserId(int userId){
        List<CreditLoan> allLoans = (List<CreditLoan>) creditLoanRepo.findAll();
        List<CreditLoansForUser> creditLoansForUsers = new ArrayList<>();
        for (CreditLoan allLoan : allLoans) {
            if (allLoan.getUser().getUserId() == userId) {
                CreditLoansForUser creditLoansForUser = new CreditLoansForUser();
                creditLoansForUser.setContractId(allLoan.getContractId());
                creditLoansForUser.setAmount(allLoan.getAmount());
                creditLoansForUser.setAmountToBePaid(allLoan.getAmountToBePaid());
                creditLoansForUser.setInterestRate(allLoan.getInterestRate());
                creditLoansForUser.setTenure(allLoan.getTenure());
                creditLoansForUser.setCreditType(allLoan.getCreditType());
                creditLoansForUser.setEsDecision(allLoan.getEsDecision());
                creditLoansForUsers.add(creditLoansForUser);
            }
        }
        return creditLoansForUsers;
    }

    public List<CreditLoanDTO> findAllLoans(){
        List<CreditLoan> allLoans = (List<CreditLoan>) creditLoanRepo.findAll();
        List<CreditLoanDTO> allLoansDTO = new ArrayList<>();
        for(CreditLoan creditLoan:allLoans){
            CreditLoanDTO creditLoanDTO = new CreditLoanDTO();
            creditLoanDTO.setContractId(creditLoan.getContractId());
            creditLoanDTO.setUser(creditLoan.getUser().getUserId());
            creditLoanDTO.setAmount(creditLoan.getAmount());
            creditLoanDTO.setAmountToBePaid(creditLoan.getAmountToBePaid());
            creditLoanDTO.setInterestRate(creditLoan.getInterestRate());
            creditLoanDTO.setTenure(creditLoan.getTenure());
            creditLoanDTO.setInstallment(creditLoan.getInstallment());
            creditLoanDTO.setCreditBureauScore(creditLoan.getCreditBureauScore());
            creditLoanDTO.setEsDecision(creditLoan.getEsDecision());
            creditLoanDTO.setCreditType(creditLoan.getCreditType());
            creditLoanDTO.setCreationDate(creditLoan.getCreationDate());
            creditLoanDTO.setLastUpdate(creditLoan.getLastUpdate());
            allLoansDTO.add(creditLoanDTO);
        }
        return  allLoansDTO;
    }

    public CreditLoan updateCreditLoanById(CreditLoanDTO creditLoanDTO){
        CreditLoan creditLoan1 = findCreditLoanById(creditLoanDTO.getContractId());
        User user = creditLoan1.getUser();
        CreditLoan creditLoan = new CreditLoan(creditLoanDTO.getContractId(), user, creditLoanDTO.getAmount(), creditLoanDTO.getInterestRate(),
                creditLoanDTO.getTenure(), creditLoanDTO.getInstallment(), creditLoanDTO.getAmountToBePaid(),
                creditLoanDTO.getCreditBureauScore(), creditLoanDTO.getEsDecision(), creditLoanDTO.getCreditType(),
                creditLoanDTO.getCreationDate(), creditLoanDTO.getLastUpdate());
        return creditLoanRepo.save(creditLoan);
    }

    public void deleteCreditLoanById(int contractId){
        CreditLoan creditLoan = findCreditLoanById(contractId);
        creditLoanRepo.delete(creditLoan);
    }
}
